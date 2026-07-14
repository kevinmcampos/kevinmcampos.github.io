package br.app.kevin.portfolio.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

// Google brand colors
private val GoogleBlue = Color(0xFF4285F4)
private val GoogleRed = Color(0xFFEA4335)
private val GoogleYellow = Color(0xFFFBBC05)
private val GoogleGreen = Color(0xFF34A853)

/** Sweep gradient that feels like Google One */
private fun googleOneSweepBrush(): Brush = Brush.sweepGradient(
    0.0f to GoogleBlue,
    0.24f to GoogleGreen,
    0.49f to GoogleYellow,
    0.74f to GoogleRed,
    1.0f to GoogleBlue,  // loop back cleanly
)

/**
 * Avatar with a progress ring that fills while you hover. Hold it long enough and the ring
 * completes, the photo swaps to [imageHover] and stays. Let go early and it rewinds, keeping
 * the first photo. Next hover resets and starts over.
 */
@Composable
fun ProfileAvatar(
    imageNormal: DrawableResource,
    imageHover: DrawableResource,
    size: Dp = 112.dp,
    ringWidth: Dp = 6.dp,
    backgroundRingWidth: Dp = 3.dp,
    startAngle: Float = -90f,
    fillDurationMs: Int = 2200,
    rewindOnExitIfNotComplete: Boolean = true,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    val ringBrush = remember { googleOneSweepBrush() }
    val ringBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)

    // completed stays true until the next hover
    val progress = remember { Animatable(0f) }
    var completed by remember { mutableStateOf(false) }

    LaunchedEffect(hovered) {
        if (hovered) {
            // hovering again after it finished: reset and refill
            if (completed) {
                completed = false
                progress.snapTo(0f)
            }
            progress.animateTo(1f, tween(durationMillis = fillDurationMs, easing = LinearEasing))
            completed = true // done: swap and keep
        } else if (!completed && rewindOnExitIfNotComplete) {
            // let go before it finished: rewind. already done: leave it filled.
            progress.animateTo(0f, tween(300, easing = LinearEasing))
        }
    }

    // tiny pulse while it's filling
    val pulse by animateFloatAsState(if (hovered && !completed) 1.03f else 1f, tween(200))

    Box(
        modifier = Modifier
            .size(size)
            .hoverable(interaction)
            .clip(CircleShape)
            .drawBehind {
                drawArc(
                    color = ringBg,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = backgroundRingWidth.toPx(), cap = StrokeCap.Round),
                )
                drawArc(
                    brush = ringBrush,
                    startAngle = startAngle,
                    sweepAngle = 360f * progress.value.coerceIn(0f, 1f),
                    useCenter = false,
                    style = Stroke(width = ringWidth.toPx(), cap = StrokeCap.Round),
                )
            }
            .padding(ringWidth),
        contentAlignment = Alignment.Center,
    ) {
        // swap on completed, not hover, so it sticks
        Crossfade(targetState = completed, label = "avatarImage") { showHoverImage ->
            Image(
                painter = painterResource(if (showHoverImage) imageHover else imageNormal),
                contentDescription = "Kevin's profile photo",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .graphicsLayer {
                        scaleX = pulse
                        scaleY = pulse
                    },
            )
        }
    }
}
