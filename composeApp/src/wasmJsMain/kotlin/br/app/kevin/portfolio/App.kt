package br.app.kevin.portfolio

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.app.kevin.portfolio.ui.ContactScreen
import br.app.kevin.portfolio.ui.ExperienceScreen
import br.app.kevin.portfolio.ui.HomeScreen
import br.app.kevin.portfolio.ui.PortfolioDestination
import br.app.kevin.portfolio.ui.components.FakeAndroidStatusBar
import br.app.kevin.portfolio.ui.components.PhoneFrame
import br.app.kevin.portfolio.ui.components.hideLoadingScreen
import br.app.kevin.portfolio.ui.theme.AppTheme

@Composable
fun App() {
    LaunchedEffect(Unit) { hideLoadingScreen() }

    AppTheme {
        PhoneFrame {
            var selectedDestination by remember { mutableStateOf(PortfolioDestination.Home) }

            Column(Modifier.fillMaxSize()) {
                FakeAndroidStatusBar()

                PortfolioNavigation(
                    selectedDestination = selectedDestination,
                    onDestinationSelected = { selectedDestination = it },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                    ) {
                        // makes the on-canvas text drag-selectable + copyable
                        SelectionContainer {
                            AnimatedContent(
                                targetState = selectedDestination,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "screen",
                            ) { destination ->
                                when (destination) {
                                    PortfolioDestination.Home -> HomeScreen()
                                    PortfolioDestination.Experience -> ExperienceScreen()
                                    PortfolioDestination.Contact -> ContactScreen()
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
    content: @Composable () -> Unit = {},
) {
    // Always a phone: bottom navigation bar, single pane. The PhoneFrame keeps the content compact
    // on every viewport, so we don't hand this over to the adaptive rail/drawer machinery.
    NavigationSuiteScaffold(
        layoutType = NavigationSuiteType.NavigationBar,
        navigationSuiteItems = {
            PortfolioDestination.entries.forEach { destination ->
                val isSelected = destination == selectedDestination
                item(
                    selected = isSelected,
                    onClick = { if (!isSelected) onDestinationSelected(destination) },
                    icon = {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            imageVector = if (isSelected) destination.selectedIcon else destination.icon,
                            contentDescription = destination.label,
                        )
                    },
                    label = {
                        Text(
                            text = destination.label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                )
            }
        },
    ) {
        content()
    }
}
