@file:OptIn(ExperimentalMaterial3Api::class)

package br.app.kevin.portfolio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Master-detail layout. Wide screens (>= 600dp) show list + detail side by side; narrow ones
 * show one at a time with a back action. Caller owns [selectedItem].
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
    masterPaneWidth: Dp = 340.dp,
    emptyDetailPane: @Composable () -> Unit = { EmptyDetailPane() },
) {
    // Measure the space we actually have (the phone screen), not the browser window, so the layout
    // is correct inside the device frame. >= 600dp gets the side-by-side two-pane treatment.
    BoxWithConstraints(modifier.fillMaxSize()) {
        val isLargeScreen = maxWidth >= 600.dp

        if (isLargeScreen) {
            Row(Modifier.fillMaxSize()) {
                Surface(
                    modifier = Modifier.width(masterPaneWidth),
                    tonalElevation = 1.dp
                ) {
                    masterPane(items, selectedItem, onItemSelected, true)
                }

                VerticalDivider()

                Surface(modifier = Modifier.weight(1f)) {
                    // local val so the null-check smart-casts
                    val current = selectedItem
                    if (current != null) {
                        detailPane(current, onBackPressed, true)
                    } else {
                        emptyDetailPane()
                    }
                }
            }
        } else {
            val current = selectedItem
            if (current == null) {
                masterPane(items, selectedItem, onItemSelected, false)
            } else {
                detailPane(current, onBackPressed, false)
            }
        }
    }
}

@Composable
private fun EmptyDetailPane() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Outlined.WorkOutline,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer16()
            Text(
                "Select a project to view details",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Spacer16() = Box(Modifier.height(16.dp))

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
    val containerColor =
        if (selected) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surface

    ListItem(
        headlineContent = {
            Text(title, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
        },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = leadingIcon,
        trailingContent = trailingIcon,
        colors = ListItemDefaults.colors(containerColor = containerColor),
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    )
}
