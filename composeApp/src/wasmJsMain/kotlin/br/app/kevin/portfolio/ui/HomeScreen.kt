package br.app.kevin.portfolio.ui

import WavingEmoji
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.app.kevin.portfolio.ContactInfo
import br.app.kevin.portfolio.ui.components.PoweredByFooter
import br.app.kevin.portfolio.ui.components.ProfileAvatar
import br.app.kevin.portfolio.ui.components.openUrl
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.Res
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.avatar
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.avatar2

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(28.dp))
            Hero()
            Spacer(Modifier.height(24.dp))
            SummaryCard()
            Spacer(Modifier.height(28.dp))
            SectionLabel("Core skills")
            Spacer(Modifier.height(14.dp))
            SkillsGrid()
            Spacer(Modifier.height(28.dp))
            SectionLabel("Get in touch")
            Spacer(Modifier.height(14.dp))
            ContactActions()
            Spacer(Modifier.height(28.dp))
        }
        PoweredByFooter()
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun Hero() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ProfileAvatar(
            size = 108.dp,
            imageNormal = Res.drawable.avatar,
            imageHover = Res.drawable.avatar2,
        )
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Hey, I'm Kevin",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.width(8.dp))
            WavingEmoji()
        }
        Spacer(Modifier.height(4.dp))
        Text(
            "Senior Mobile Engineer",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(14.dp))
        AvailabilityPill()
    }
}

@Composable
private fun AvailabilityPill() {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
        ) {
            Box(
                Modifier
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF22C55E)),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Open to senior mobile roles · Porto, Portugal",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun SummaryCard() {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            "10 years shipping apps across Android and iOS. I specialize in Kotlin Multiplatform — " +
                "one shared codebase powering Android, iOS, tvOS, and web, with native foundations on " +
                "both sides. Apps at 1M+ MAU and 99.9% crash-free.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(20.dp),
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.2.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start,
    )
}

@Composable
private fun SkillsGrid() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        SkillChip("KMP", Icons.Filled.Devices)
        SkillChip("Kotlin", Icons.Filled.Code)
        SkillChip("Swift", Icons.Filled.Bolt)
        SkillChip("Compose", Icons.Filled.TouchApp)
        SkillChip("Android", Icons.Filled.Android)
        SkillChip("iOS", Icons.Filled.PhoneIphone)
    }
}

@Composable
private fun ContactActions() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = { openUrl(ContactInfo.mailto(subject = "Hello Kevin")) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Filled.Email, null, Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Email me")
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedButton(onClick = { openUrl(ContactInfo.GITHUB) }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Filled.Code, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("GitHub")
            }
            OutlinedButton(onClick = { openUrl(ContactInfo.LINKEDIN) }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Filled.Work, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("LinkedIn")
            }
            OutlinedButton(onClick = { openUrl(ContactInfo.CV_URL) }, modifier = Modifier.weight(1f)) {
                Icon(Icons.Filled.Download, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("CV")
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

    val bg = if (hovered) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest
    val border = if (hovered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val content = if (hovered) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(BorderStroke(1.dp, border), RoundedCornerShape(12.dp))
            .hoverable(interactionSource = interaction, enabled = true)
            .padding(horizontal = 14.dp, vertical = 10.dp),
    ) {
        Icon(icon, null, tint = content, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = content,
        )
    }
}
