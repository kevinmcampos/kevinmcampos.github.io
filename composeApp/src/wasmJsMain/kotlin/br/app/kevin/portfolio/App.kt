package br.app.kevin.portfolio

import WavingEmoji
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import br.app.kevin.portfolio.ui.ExperienceScreen
import br.app.kevin.portfolio.ui.components.FakeAndroidStatusBar
import br.app.kevin.portfolio.ui.PortfolioDestination
import br.app.kevin.portfolio.ui.components.ProfileHoverAvatarGoogleOnePersistent
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.Res
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.avatar
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.avatar2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun App() {
    MaterialTheme {
        Column {
            FakeAndroidStatusBar()

            PortfolioNavigation {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .safeContentPadding()
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row {
                        ProfileHoverAvatarGoogleOnePersistent(
                            size = 128.dp,
                            imageNormal = Res.drawable.avatar,
                            imageHover = Res.drawable.avatar2,
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Row {
                                Text("Hello, I'm Kevin Campos", style = MaterialTheme.typography.headlineMedium, fontWeight = SemiBold)
                                WavingEmoji()
                            }
                            Row {

                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    imageVector = Icons.Default.Android,
                                    contentDescription = "Reply"
                                )
                                Text("Android Engineer", style = MaterialTheme.typography.headlineSmall, fontWeight = SemiBold)
                            }
                            Row {

                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    imageVector = Icons.Outlined.PinDrop,
                                    contentDescription = "Reply"
                                )
                                Text("Porto, Portugal", style = MaterialTheme.typography.headlineSmall, fontWeight = SemiBold)
                            }
                        }
                    }

                    Row {
                        Spacer(Modifier.weight(1f))
                        HeroSection()
                        Spacer(Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(24.dp))

                    ExperienceScreen()
                }
            }
        }
    }
}

@Composable
private fun PortfolioNavigation(
    content: @Composable () -> Unit = {}
) {
    var selectedDestination: PortfolioDestination by remember {
        mutableStateOf(PortfolioDestination.Home)
    }
    val layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
        adaptiveInfo = currentWindowAdaptiveInfo()
    )

    NavigationSuiteScaffold(
        layoutType = layoutType,
        navigationSuiteItems = {
            PortfolioDestination.entries.forEachIndexed { index, destination ->
                val modifier = if (layoutType == NavigationSuiteType.NavigationRail) {
                    val topPadding =
                        if (index == 0 && layoutType == NavigationSuiteType.NavigationRail) 30.dp else 0.dp
                    val bottomPadding =
                        if (index == PortfolioDestination.entries.size - 1 && layoutType == NavigationSuiteType.NavigationRail) 56.dp else 0.dp
                    Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        .padding(top = topPadding, bottom = bottomPadding)
                } else {
                    Modifier
                }

                val isSelected: Boolean = destination == selectedDestination

                item(
                    modifier = modifier,
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            selectedDestination = destination
                        }
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = if (isSelected) {
                                destination.selectedIcon
                            } else {
                                destination.icon
                            },
                            contentDescription = destination.label,
                        )
                    },
                    label = {
                        Text(
                            text = destination.label,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                )
            }
        }
    ) {
        content()
    }
}

// ---------- Small building blocks

@Composable
private fun AvailabilityDot(
    color: Color = Color(0xFF22C55E), // green
    size: Dp = 10.dp
) {
    Box(
        Modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}





@Composable
private fun HoverIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    baseTint: Color = Color(0xFF9CA3AF),          // gray-400
    hoverTint: Color = Color(0xFFF59E0B),         // amber-500
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val int: Int? = 23
    int ?: 2

    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (hovered) hoverTint else baseTint,
        modifier = modifier
            .padding(end = 8.dp)
            // using the interaction source for hover without ripple
            .hoverableNoRipple(interaction)
    )
}

/** Minimal hoverable without ripple for MPP */
@Composable
private fun Modifier.hoverableNoRipple(
    interactionSource: MutableInteractionSource
) = this.then(
    Modifier.hoverable(
        interactionSource = interactionSource,
        enabled = true
    )
)

@Composable
private fun SkillChip(
    label: String,
    icon: ImageVector,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    val bg = if (hovered) Color(0xFF111827) else Color(0xFF0B0F17) // subtle lift
    val border = if (hovered) Color(0xFF1F2937) else Color(0xFF111827)
    val tint = if (hovered) Color(0xFFF59E0B) else Color(0xFF9CA3AF)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .border(BorderStroke(1.dp, border), RoundedCornerShape(16.dp))
            .hoverableNoRipple(interaction)
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(10.dp))
        Text(
            label,
            style = MaterialTheme.typography.titleSmall.copy(
                color = Color(0xFFE5E7EB), // gray-200
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

// ---------- Header like the screenshot

@Composable
private fun HeroHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ProfileHoverAvatarGoogleOnePersistent(
            size = 96.dp,
            imageNormal = Res.drawable.avatar,
            imageHover = Res.drawable.avatar2,
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Hey, I'm Kevin (keh-vin)",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF9FAFB)
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
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF9CA3AF)
                )
            }
        }
    }
}

// ---------- Whole section

@Composable
private fun HeroSection() {
    Column(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .background(Color(0xFF0B0F14)) // deep slate
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        HeroHeader()
        Spacer(Modifier.height(16.dp))
        Text(
            "I'm an Android Engineer with ten years of experience. I specialize in delivering high-performing apps and SDKs. Expert in Kotlin, Jetpack Compose, and modern Android architecture.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF9CA3AF)
        )
        Spacer(Modifier.height(24.dp))

        // Skills grid (wrap like the screenshot)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SkillChip("Android", Icons.Filled.Android)
            SkillChip("Kotlin", Icons.Filled.Code)
            SkillChip("Compose", Icons.Filled.TouchApp)
            SkillChip("KMP", Icons.Filled.Devices)
            SkillChip("TypeScript", Icons.Filled.Language)
            SkillChip("CI/CD", Icons.Filled.Code)
        }
    }
}
