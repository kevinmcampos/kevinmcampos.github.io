package br.app.kevin.portfolio.ui

import WavingEmoji
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.app.kevin.portfolio.ui.components.ProfileHoverAvatarGoogleOnePersistent
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.Res
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.avatar
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.avatar2

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeroSection()
    }
}

@Composable
private fun HeroSection() {
    Column(
        modifier = Modifier
            .widthIn(max = 800.dp)
            .padding(24.dp)
    ) {
        HeroHeader()
        Spacer(Modifier.height(32.dp))
        Text(
            "I'm an Android Engineer with ten years of experience. I specialize in delivering high-performing apps and SDKs. Expert in Kotlin, Jetpack Compose, and modern Android architecture.",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))

        // Skills grid
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SkillChip("Android", Icons.Filled.Android)
            SkillChip("Kotlin", Icons.Filled.Code)
            SkillChip("Compose", Icons.Filled.TouchApp)
            SkillChip("KMP", Icons.Filled.Devices)
            SkillChip("TypeScript", Icons.Filled.Language)
            SkillChip("CI/CD", Icons.Filled.Build)
        }
    }
}

@Composable
private fun HeroHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ProfileHoverAvatarGoogleOnePersistent(
            size = 120.dp,
            imageNormal = Res.drawable.avatar,
            imageHover = Res.drawable.avatar2,
        )
        Spacer(Modifier.width(24.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Hey, I'm Kevin",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.width(8.dp))
                WavingEmoji()
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvailabilityDot()
                Spacer(Modifier.width(8.dp))
                Text(
                    "Available for work",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SkillChip(
    label: String,
    icon: ImageVector,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    val bg = if (hovered) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val border = if (hovered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .border(BorderStroke(1.dp, border), RoundedCornerShape(16.dp))
            .hoverable(interactionSource = interaction, enabled = true)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun AvailabilityDot(
    color: Color = Color(0xFF22C55E), // green
    size: Dp = 12.dp
) {
    Box(
        Modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}
