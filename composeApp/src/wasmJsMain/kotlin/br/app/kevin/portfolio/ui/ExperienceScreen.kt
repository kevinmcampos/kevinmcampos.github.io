@file:OptIn(ExperimentalMaterial3Api::class)

package br.app.kevin.portfolio.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.app.kevin.portfolio.ui.components.AdaptiveMasterDetailScaffold
import br.app.kevin.portfolio.ui.components.MasterDetailTopBar
import br.app.kevin.portfolio.ui.components.MasterListItem
import br.app.kevin.portfolio.ui.components.ScreenshotCarousel
import br.app.kevin.portfolio.ui.components.openUrl

@Composable
fun ExperienceScreen() {
    val projects = remember { experiences }

    var selectedProject by remember { mutableStateOf<ProjectItem?>(null) }

    AdaptiveMasterDetailScaffold(
        items = projects,
        selectedItem = selectedProject,
        onItemSelected = { project -> selectedProject = project },
        onBackPressed = { selectedProject = null },
        masterPane = { items, selected, onItemClick, isLargeScreen ->
            ProjectList(
                projects = items,
                selectedProject = selected,
                onProjectClick = onItemClick,
                isLargeScreen = isLargeScreen
            )
        },
        detailPane = { project, onBack, isLargeScreen ->
            ProjectDetail(
                project = project,
                onBackClick = onBack,
                isLargeScreen = isLargeScreen
            )
        }
    )
}

@Composable
private fun ProjectList(
    projects: List<ProjectItem>,
    selectedProject: ProjectItem?,
    onProjectClick: (ProjectItem) -> Unit,
    isLargeScreen: Boolean
) {
    Column {
        MasterDetailTopBar(title = "Experience")

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(projects, key = { it.id }) { project ->
                ProjectListItem(
                    project = project,
                    selected = selectedProject?.id == project.id && isLargeScreen,
                    onClick = { onProjectClick(project) }
                )
            }
        }
    }
}

@Composable
private fun ProjectListItem(
    project: ProjectItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    MasterListItem(
        title = project.title,
        subtitle = "${project.role} • ${project.period}",
        selected = selected,
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Android,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Composable
private fun ProjectDetail(
    project: ProjectItem,
    onBackClick: () -> Unit,
    isLargeScreen: Boolean
) {
    Column(Modifier.fillMaxSize()) {
        MasterDetailTopBar(
            title = project.title,
            showBackButton = !isLargeScreen,
            onBackClick = onBackClick,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = project.role,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = project.period,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            project.link?.let { url ->
                Spacer(Modifier.height(16.dp))
                OutlinedButton(onClick = { openUrl(url) }) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Open project")
                }
            }

            if (project.screenshots.isNotEmpty()) {
                Spacer(Modifier.height(24.dp))
                SectionTitle("Screenshots")
                Spacer(Modifier.height(12.dp))
                ScreenshotCarousel(shots = project.screenshots)
            }

            Spacer(Modifier.height(24.dp))
            SectionTitle("About")
            Spacer(Modifier.height(8.dp))
            Text(
                text = project.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))
            SectionTitle("Technologies")
            Spacer(Modifier.height(12.dp))

            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                project.technologies.forEach { tech ->
                    SuggestionChip(
                        onClick = { },
                        label = { Text(tech) }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
    )
}
