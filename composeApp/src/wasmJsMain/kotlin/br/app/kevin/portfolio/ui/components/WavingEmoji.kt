import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.Res
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.wave
import org.jetbrains.compose.resources.painterResource

@Composable
fun WavingEmoji() {
    val rot = remember { Animatable(0f) }
    val baseTilt = 8f
    val waveA = 18f

    val pivot = TransformOrigin(0.85f, 0.85f) // bottom-right

    val seq = floatArrayOf(
        baseTilt + waveA,
        baseTilt - 12f,
        baseTilt + 16f,
        baseTilt - 8f,
        baseTilt + 12f,
        baseTilt
    )

    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    LaunchedEffect(hovered) {
        if (hovered) {
            // Start from base tilt, then loop a few sways
            rot.snapTo(baseTilt)
            while (true) {
                for (target in seq) {
                    rot.animateTo(target, tween(durationMillis = 140, easing = FastOutSlowInEasing))
                }
            }
        } else {
            rot.animateTo(
                0f,
                tween(220, easing = FastOutSlowInEasing)
            ) // settle flat when not hovered
        }
    }

    Image(
        painter = painterResource(Res.drawable.wave),
        contentDescription = "Wave",
        modifier = Modifier
            .hoverable(interaction)
            .size(32.dp)
            .graphicsLayer {
                transformOrigin = pivot
                rotationZ = rot.value
            }
    )
}
