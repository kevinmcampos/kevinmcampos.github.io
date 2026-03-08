package br.app.kevin.portfolio

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.app.kevin.portfolio.ui.ContactScreen
import br.app.kevin.portfolio.ui.ExperienceScreen
import br.app.kevin.portfolio.ui.HomeScreen
import br.app.kevin.portfolio.ui.PortfolioDestination
import br.app.kevin.portfolio.ui.components.FakeAndroidStatusBar

@Composable
fun App() {
    MaterialTheme {
        Column {
            FakeAndroidStatusBar()

            var selectedDestination by remember { mutableStateOf(PortfolioDestination.Home) }

            PortfolioNavigation(
                selectedDestination = selectedDestination,
                onDestinationSelected = { selectedDestination = it }
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .safeContentPadding()
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedContent(targetState = selectedDestination) { destination ->
                        when (destination) {
                            PortfolioDestination.Home -> HomeScreen()
                            PortfolioDestination.Experience -> ExperienceScreen()
                            PortfolioDestination.Contact -> ContactScreen()
                            PortfolioDestination.Settings -> {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("Settings Placeholder")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PortfolioNavigation(
    selectedDestination: PortfolioDestination,
    onDestinationSelected: (PortfolioDestination) -> Unit,
    content: @Composable () -> Unit = {}
) {
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
                            onDestinationSelected(destination)
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
