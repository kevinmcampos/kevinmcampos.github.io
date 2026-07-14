package br.app.kevin.portfolio.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.filled.SignalWifi4Bar
import androidx.compose.material.icons.outlined.NotificationsNone
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Fake android status bar, just for the vibe. Icons are decorative (null contentDescription,
 * so screen readers skip them). The clock is the visitor's real local time, and the battery
 * level tracks the time of day — high in the morning, draining through the day, charging overnight.
 */
@Composable
fun FakeAndroidStatusBar(
    modifier: Modifier = Modifier,
    height: Dp = 30.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
) {
    val bg = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    val fg = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        color = bg,
        contentColor = fg,
    ) {
        val minutesOfDay by rememberMinuteClock()
        val (level, charging) = batteryForMinuteOfDay(minutesOfDay)

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = formatClock(minutesOfDay),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = fg,
            )

            Spacer(Modifier.weight(1f))

            val iconMod = Modifier.size(15.dp)
            Icon(Icons.Outlined.NotificationsNone, null, iconMod, tint = fg)
            Spacer(Modifier.width(7.dp))
            Icon(Icons.Default.SignalWifi4Bar, null, iconMod, tint = fg)
            Spacer(Modifier.width(6.dp))
            Icon(Icons.Default.SignalCellular4Bar, null, iconMod, tint = fg)
            Spacer(Modifier.width(7.dp))
            BatteryIndicator(level = level, charging = charging, tint = fg)
            Spacer(Modifier.width(5.dp))
            Text(
                text = "$level%",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = fg,
            )
        }
    }
}

/** A horizontal battery pill whose fill tracks [level] (0..100). Turns amber/red when low. */
@Composable
private fun BatteryIndicator(level: Int, charging: Boolean, tint: Color) {
    val fill = when {
        level <= 15 -> Color(0xFFE53935) // red
        level <= 30 -> Color(0xFFF9A825) // amber
        else -> Color(0xFF34A853) // green
    }
    Canvas(Modifier.size(width = 24.dp, height = 12.dp)) {
        val strokeW = size.height * 0.12f
        val capW = size.width * 0.06f
        val bodyW = size.width - capW
        val bodyH = size.height
        val r = bodyH * 0.28f

        // outline
        drawRoundRect(
            color = tint.copy(alpha = 0.7f),
            topLeft = Offset(strokeW / 2, strokeW / 2),
            size = Size(bodyW - strokeW, bodyH - strokeW),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(r, r),
            style = Stroke(width = strokeW),
        )
        // positive terminal cap
        drawRoundRect(
            color = tint.copy(alpha = 0.7f),
            topLeft = Offset(bodyW, bodyH * 0.3f),
            size = Size(capW, bodyH * 0.4f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(capW, capW),
        )
        // fill
        val pad = strokeW * 1.6f
        val maxFillW = bodyW - strokeW - pad * 2
        val fillW = (maxFillW * (level.coerceIn(0, 100) / 100f))
        if (fillW > 0f) {
            drawRoundRect(
                color = fill,
                topLeft = Offset(strokeW / 2 + pad, strokeW / 2 + pad),
                size = Size(fillW, bodyH - strokeW - pad * 2),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.6f, r * 0.6f),
            )
        }
        // charging bolt
        if (charging) {
            val cx = bodyW / 2
            val cy = bodyH / 2
            val bolt = Path().apply {
                moveTo(cx + bodyH * 0.10f, bodyH * 0.18f)
                lineTo(cx - bodyH * 0.18f, cy + bodyH * 0.04f)
                lineTo(cx + bodyH * 0.02f, cy + bodyH * 0.04f)
                lineTo(cx - bodyH * 0.10f, bodyH * 0.82f)
                lineTo(cx + bodyH * 0.22f, cy - bodyH * 0.06f)
                lineTo(cx + bodyH * 0.02f, cy - bodyH * 0.06f)
                close()
            }
            drawPath(bolt, color = Color.White)
            drawPath(bolt, color = tint.copy(alpha = 0.35f), style = Stroke(width = strokeW * 0.6f, cap = StrokeCap.Round))
        }
    }
}

/**
 * Battery as a function of the local time of day. Unplugged at 07:00 (100%), drains linearly to
 * ~20% by 23:00, then charges overnight back to full by 07:00. Purely cosmetic.
 */
private fun batteryForMinuteOfDay(minute: Int): Pair<Int, Boolean> {
    val wakeStart = 7 * 60      // 07:00
    val nightStart = 23 * 60    // 23:00
    return if (minute in wakeStart until nightStart) {
        val progress = (minute - wakeStart).toFloat() / (nightStart - wakeStart)
        val level = (100 - 80 * progress).toInt().coerceIn(20, 100)
        level to false
    } else {
        // overnight charge window (23:00 -> 07:00), wrapping past midnight
        val intoCharge = if (minute >= nightStart) minute - nightStart else minute + (1440 - nightStart)
        val total = (1440 - nightStart) + wakeStart
        val progress = intoCharge.toFloat() / total
        val level = (20 + 80 * progress).toInt().coerceIn(20, 100)
        level to true
    }
}

private fun formatClock(minuteOfDay: Int): String {
    val h = minuteOfDay / 60
    val m = minuteOfDay % 60
    val hh = if (h < 10) "0$h" else "$h"
    val mm = if (m < 10) "0$m" else "$m"
    return "$hh:$mm"
}

/** Local minute-of-day (0..1439), refreshed every 30s. */
@Composable
private fun rememberMinuteClock(): State<Int> {
    val state = remember { mutableStateOf(currentMinuteOfDay()) }
    LaunchedEffect(Unit) {
        while (true) {
            state.value = currentMinuteOfDay()
            delay(30_000L)
        }
    }
    return state
}

private fun currentMinuteOfDay(): Int =
    js("(() => { const d = new Date(); return d.getHours() * 60 + d.getMinutes(); })()")
