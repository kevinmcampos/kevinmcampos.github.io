package br.app.kevin.portfolio.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import org.jetbrains.compose.resources.ExperimentalResourceApi
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProfileHoverAvatarGoogleOnePersistent(
    imageNormal: DrawableResource,
    imageHover: DrawableResource,
    size: Dp = 112.dp,
    ringWidth: Dp = 6.dp,
    backgroundRingWidth: Dp = 3.dp,
    startAngle: Float = -90f,
    fillDurationMs: Int = 2200,   // ← slower fill; tweak as you like
    rewindOnExitIfNotComplete: Boolean = true
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val ringBrush = remember { googleOneSweepBrush() }
    val ringBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)

    // Fine-grained progress control
    val progress = remember { Animatable(0f) }
    var completed by remember { mutableStateOf(false) }

    // Keep a small hover pulse only while hovering (not while "completed & idle")
    val pulse = if (hovered) 1.03f else 1f

    LaunchedEffect(hovered) {
        if (hovered) {
            // If user hovers again after completion: reset and start a new cycle
            if (completed) {
                completed = false
                progress.snapTo(0f)
            }
            // Fill to 100%
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = fillDurationMs, easing = LinearEasing)
            )
            // Mark as completed (stick at 1f until next hover)
            completed = true
        } else {
            // On exit: if not completed yet, optionally rewind to 0
            if (!completed && rewindOnExitIfNotComplete) {
                progress.animateTo(0f, animationSpec = tween(300, easing = LinearEasing))
            }
            // If completed, do nothing → stays at 1f
        }
    }

    Box(
        modifier = Modifier
            .size(size)
            .hoverable(interaction)
            .clip(CircleShape)
            .drawBehind {
                val stroke = Stroke(width = ringWidth.toPx(), cap = StrokeCap.Round)
                val backgroundStoke = Stroke(width = backgroundRingWidth.toPx(), cap = StrokeCap.Round)
                // Background ring
                drawArc(
                    color = ringBg, startAngle = 0f, sweepAngle = 360f,
                    useCenter = false, style = backgroundStoke
                )
                // Progress ring
                drawArc(
                    brush = ringBrush,
                    startAngle = startAngle,
                    sweepAngle = 360f * progress.value.coerceIn(0f, 1f),
                    useCenter = false,
                    style = stroke
                )
            }
            .padding(ringWidth),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(targetState = completed, label = "imgSwap") { isHovered ->
            Image(
                painter = painterResource(if (isHovered) imageHover else imageNormal),
                contentDescription = "Profile",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .graphicsLayer {
                        scaleX = pulse
                        scaleY = pulse
                    }
            )
        }
    }
}
