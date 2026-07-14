package br.app.kevin.portfolio.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * One screenshot in a project's carousel.
 *
 * TO ADD A REAL SCREENSHOT: drop the PNG/JPEG into
 * `composeApp/src/wasmJsMain/composeResources/drawable/` (e.g. `argos_home.png`), let Gradle
 * regenerate `Res`, then pass `image = Res.drawable.argos_home` here. When [image] is null the card
 * renders a tasteful gradient placeholder using [label] so the layout is complete without assets.
 */
data class ProjectShot(
    val label: String,
    val image: DrawableResource? = null,
)

private val cardWidth = 158.dp
private val cardHeight = 320.dp

/** Play-Store-style horizontal carousel of phone screenshots. Scrolls horizontally. */
@Composable
fun ScreenshotCarousel(
    shots: List<ProjectShot>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        itemsIndexed(shots) { index, shot ->
            ScreenshotCard(shot = shot, index = index)
        }
    }
}

@Composable
private fun ScreenshotCard(shot: ProjectShot, index: Int) {
    Box(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        contentAlignment = Alignment.Center,
    ) {
        val image = shot.image
        if (image != null) {
            Image(
                painter = painterResource(image),
                contentDescription = "${shot.label} screenshot",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            PlaceholderShot(label = shot.label, index = index)
        }
    }
}

@Composable
private fun PlaceholderShot(label: String, index: Int) {
    // rotating set of soft gradients so a row of placeholders reads as a set, not a mistake
    val gradients = listOf(
        listOf(Color(0xFF6750A4), Color(0xFF9A82DB)),
        listOf(Color(0xFF386A20), Color(0xFF6DA95B)),
        listOf(Color(0xFF00639B), Color(0xFF4AA3D6)),
        listOf(Color(0xFF7D5260), Color(0xFFB98495)),
        listOf(Color(0xFF8A5000), Color(0xFFD69A4A)),
    )
    val colors = gradients[index % gradients.size]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(colors)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Outlined.PhoneAndroid,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp),
                )
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
        }
        // caption strip, Play-Store-ish
        Text(
            text = "Preview",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.75f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp),
        )
    }
}
