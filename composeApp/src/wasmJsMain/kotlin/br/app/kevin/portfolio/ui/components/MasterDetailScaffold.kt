@file:OptIn(ExperimentalMaterial3Api::class)

package br.app.kevin.portfolio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass

/**
 * A simplified master-detail layout that adapts based on screen size.
 * On large screens, shows both master and detail panes side by side.
 * On small screens, shows only one pane at a time with navigation.
 */
@Composable
fun MasterDetailScaffold(
    selectedItem: Any?,
    onItemSelected: (Any?) -> Unit,
    masterPane: @Composable () -> Unit,
    detailPane: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    masterPaneWidth: Dp = 600.dp,
    showDetailPane: Boolean = selectedItem != null,
    emptyDetailPane: @Composable () -> Unit = {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Select an item to view details")
        }
    }
) {
    val isLargeScreen = true // configuration.screenWidthDp.dp >= 600.dp

    Row(modifier = modifier.fillMaxSize()) {
        if (isLargeScreen) {
            // Large screen: Show both panes
            Surface(
                modifier = Modifier.width(masterPaneWidth),
                tonalElevation = 1.dp
            ) {
                masterPane()
            }

            HorizontalDivider()

            Surface(
                modifier = Modifier.weight(1f)
            ) {
                if (showDetailPane) {
                    detailPane()
                } else {
                    emptyDetailPane()
                }
            }
        } else {
            // Small screen: Show one pane at a time
            if (!showDetailPane) {
                masterPane()
            } else {
                detailPane()
            }
        }
    }
}

/**
 * State holder for master-detail navigation
 */
@Stable
class MasterDetailState<T>(
    initialSelectedItem: T? = null
) {
    var selectedItem by mutableStateOf(initialSelectedItem)
        private set

    val isDetailShown: Boolean
        get() = selectedItem != null

    val isLargeScreen = mutableStateOf(false)

    fun selectItem(item: T?) {
        selectedItem = item
    }

    fun clearSelection() {
        selectedItem = null
    }

    fun navigateBack(): Boolean {
        return if (selectedItem != null && !isLargeScreen.value) {
            clearSelection()
            true
        } else {
            false
        }
    }
}

/**
 * Remember a master-detail state
 */
@Composable
fun <T> rememberMasterDetailState(
    initialSelectedItem: T? = null
): MasterDetailState<T> {
    return remember { MasterDetailState(initialSelectedItem) }
}

/**
 * Enhanced version with navigation integration and back handling
 */
@Composable
fun <T> AdaptiveMasterDetailScaffold(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    onBackPressed: () -> Unit,
    masterPane: @Composable (
        items: List<T>,
        selectedItem: T?,
        onItemClick: (T) -> Unit,
        isLargeScreen: Boolean
    ) -> Unit,
    detailPane: @Composable (
        item: T,
        onBackClick: () -> Unit,
        isLargeScreen: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier,
    masterPaneWidth: Dp = 320.dp,
    emptyDetailPane: @Composable () -> Unit = {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Select an item to view details",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val isLargeScreen =
        windowClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    val hasSelectedItem = selectedItem != null

    // Handle back navigation for small screens
    LaunchedEffect(selectedItem, isLargeScreen) {
        // Update state based on screen size changes
    }

    Row(modifier = modifier.fillMaxSize()) {
        if (isLargeScreen || !hasSelectedItem) {
            // Show master pane
            Surface(
                modifier = if (isLargeScreen) {
                    Modifier.width(masterPaneWidth)
                } else {
                    Modifier.fillMaxSize()
                },
                tonalElevation = if (isLargeScreen) 1.dp else 0.dp
            ) {
                masterPane(
                    items,
                    selectedItem,
                    onItemSelected,
                    isLargeScreen
                )
            }
        }

        if (isLargeScreen) {
            VerticalDivider()
        }

        if (isLargeScreen || hasSelectedItem) {
            // Show detail pane
            Surface(
                modifier = if (isLargeScreen) {
                    Modifier.weight(1f)
                } else {
                    Modifier.fillMaxSize()
                }
            ) {
                if (hasSelectedItem) {
                    detailPane(
                        selectedItem,
                        onBackPressed,
                        isLargeScreen
                    )
                } else if (isLargeScreen) {
                    emptyDetailPane()
                }
            }
        }
    }
}

// Helper composables for common UI patterns

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasterDetailTopBar(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = actions
    )
}

@Composable
fun MasterListItem(
    title: String,
    subtitle: String? = null,
    selected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = leadingIcon,
        trailingContent = trailingIcon,
        modifier = modifier
            .clickable { onClick() }
            .let {
                if (selected) {
                    it.background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                } else it
            }
    )
}