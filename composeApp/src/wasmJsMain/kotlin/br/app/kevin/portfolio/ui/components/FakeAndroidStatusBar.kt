@file:OptIn(ExperimentalTime::class)

package br.app.kevin.portfolio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.filled.SignalWifi4Bar
import androidx.compose.material.icons.outlined.DoNotDisturbOn
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


/**
 * A lightweight, cross-platform "fake" Android status bar for previews (web/desktop).
 *
 * - Set [height] to ~24.dp to mimic Android phones (28–32.dp also looks good on desktop zoom levels)
 * - [darkIcons] switches between dark/light icon look
 * - Optional [notch] cutout to resemble modern phones
 */
@Composable
fun FakeAndroidStatusBar(
    modifier: Modifier = Modifier,
    height: Dp = 28.dp,
    darkIcons: Boolean = true,
    showNotch: Boolean = false,
    notchWidth: Dp = 120.dp,
    notchHeight: Dp = 20.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp)
) {
    val bg = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    val fg = if (darkIcons) Color.Black.copy(alpha = 0.80f) else Color.White
    val dim = fg.copy(alpha = 0.65f)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        color = bg,
        contentColor = fg,
        shadowElevation = 0.dp
    ) {
        Box(Modifier.fillMaxSize()) {
            // Optional notch/cutout
            if (showNotch) {
                Box(
                    Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-4).dp)
                        .size(width = notchWidth, height = notchHeight)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                        )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Time (updates every minute)
                val time by rememberTimeText()
                Text(
                    text = time,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = fg
                )

                Spacer(Modifier.width(8.dp))

                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.BugReport,
                    contentDescription = "Reply"
                )

                Spacer(Modifier.weight(1f))

                // Right-side status icons cluster
                StatusIconsRow(
                    tint = fg,
                    dimTint = dim
                )
            }
        }
    }
}

@Composable
private fun StatusIconsRow(
    tint: Color,
    dimTint: Color,
    spacing: Dp = 8.dp
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier.size(16.dp), imageVector = Icons.Outlined.NotificationsOff, contentDescription = "Reply")

        Spacer(Modifier.width(8.dp))

        // TODO: Night mode if at night

        Icon(modifier = Modifier.size(16.dp), imageVector = Icons.Outlined.DoNotDisturbOn, contentDescription = "Reply")

        Spacer(Modifier.width(8.dp))

        Icon(modifier = Modifier.size(16.dp), imageVector = Icons.Default.SignalWifi4Bar, contentDescription = "Reply")

        Icon(modifier = Modifier.size(16.dp), imageVector = Icons.Default.SignalCellular4Bar, contentDescription = "Reply")

        Spacer(Modifier.width(4.dp))

        Icon(modifier = Modifier.size(16.dp), imageVector = Icons.Default.BatteryChargingFull, contentDescription = "Reply")

        Text(
            text = "96%",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )

//        Icon(modifier = Modifier.size(16.dp), imageVector = Icons.Default.Percent, contentDescription = "Reply")

    }
}

/** Keeps a HH:mm string that refreshes every minute. */
@Composable
private fun rememberTimeText(): State<String> {
    val state = remember { mutableStateOf(currentTimeString()) }
    LaunchedEffect(Unit) {
        // align to minute boundaries roughly
        while (true) {
            val nextMinute = 60_000L
            delay(nextMinute)
            state.value = currentTimeString()
        }
    }
    return state
}

private fun currentTimeString(): String {
    // Keep it simple & locale-agnostic for previews
    val now = Clock.System.now()
//    val local = now.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
    val hh = "00" // local.hour.toString().padStart(2, '0')
    val mm = "22" // local.minute.toString().padStart(2, '0')
    return "$hh:$mm"
}