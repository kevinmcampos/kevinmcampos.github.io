package br.app.kevin.portfolio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * The site's conceit is an Android phone. On a roomy viewport (a recruiter's laptop) we render a
 * real device mockup: the app lives inside a bezelled, rounded, shadowed handset floating on a soft
 * branded backdrop. On an actual phone-sized viewport there's no room for that, so the app goes
 * edge-to-edge and *is* the phone. [content] is always laid out at a compact, phone width, so the
 * UI inside can assume a single-pane, bottom-nav layout everywhere.
 */
@Composable
fun PhoneFrame(content: @Composable () -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        // enough room for a framed handset plus breathing margins?
        val framed = maxWidth >= 720.dp && maxHeight >= 640.dp

        if (!framed) {
            content()
            return@BoxWithConstraints
        }

        val phoneWidth = 400.dp
        val phoneHeight = minOf(maxHeight - 48.dp, 860.dp)
        val bezel = 12.dp
        val screenRadius = RoundedCornerShape(30.dp)
        val bodyRadius = RoundedCornerShape(44.dp)
        val dark = MaterialTheme.colorScheme.background.luminanceIsDark()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(deskBackdrop(dark)),
            contentAlignment = Alignment.Center,
        ) {
            // phone body (bezel)
            Box(
                modifier = Modifier
                    .width(phoneWidth + bezel * 2)
                    .height(phoneHeight + bezel * 2)
                    .shadow(elevation = 40.dp, shape = bodyRadius, clip = false)
                    .clip(bodyRadius)
                    .background(if (dark) Color(0xFF2A2A30) else Color(0xFF121216))
                    .padding(bezel),
                contentAlignment = Alignment.Center,
            ) {
                // screen
                Surface(
                    modifier = Modifier
                        .width(phoneWidth)
                        .height(phoneHeight)
                        .clip(screenRadius),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    content()
                }
            }
        }
    }
}

private fun Color.luminanceIsDark(): Boolean =
    (0.299f * red + 0.587f * green + 0.114f * blue) < 0.5f

private fun deskBackdrop(dark: Boolean): Brush =
    if (dark) {
        Brush.linearGradient(
            listOf(Color(0xFF16151A), Color(0xFF201B2B), Color(0xFF121016)),
        )
    } else {
        Brush.linearGradient(
            listOf(Color(0xFFF3EEFC), Color(0xFFE9E1FB), Color(0xFFF6F2FF)),
        )
    }
