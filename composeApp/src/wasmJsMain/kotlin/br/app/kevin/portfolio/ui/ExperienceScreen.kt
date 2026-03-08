@file:OptIn(ExperimentalMaterial3Api::class)

package br.app.kevin.portfolio.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import br.app.kevin.portfolio.ui.components.AdaptiveMasterDetailScaffold
import br.app.kevin.portfolio.ui.components.MasterDetailTopBar
import br.app.kevin.portfolio.ui.components.MasterListItem
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.Res
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.avatar
import kevin_portfolio_wasm_kmp.composeapp.generated.resources.avatar2
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

data class ProjectItem(
    val id: String,
    val title: String,
    val role: String,
    val description: String,
    val technologies: List<String>,
    val period: String,
    val imageRes: DrawableResource? = null
)

@Composable
fun ExperienceScreen() {
    val projects = remember {
        listOf(
            ProjectItem(
                id = "1",
                title = "E-Commerce Android App",
                role = "Lead Android Engineer",
                description = "Led the development of a feature-rich e-commerce application with over 1M downloads. Implemented a modular architecture using MVVM and Clean Architecture principles. Integrated payment gateways, push notifications, and real-time order tracking.",
                technologies = listOf("Kotlin", "Jetpack Compose", "Coroutines", "Hilt", "Room"),
                period = "2021 - Present",
                imageRes = Res.drawable.avatar // Placeholder
            ),
            ProjectItem(
                id = "2",
                title = "Banking SDK",
                role = "Senior Android Developer",
                description = "Developed a secure banking SDK used by multiple financial institutions. Focused on security, performance, and API design. Implemented biometric authentication and encrypted data storage.",
                technologies = listOf("Kotlin", "Security Crypto", "Retrofit", "OkHttp", "JUnit"),
                period = "2019 - 2021",
                imageRes = Res.drawable.avatar2 // Placeholder
            ),
            ProjectItem(
                id = "3",
                title = "Social Media Platform",
                role = "Android Developer",
                description = "Contributed to a social media app focused on photo sharing. Optimized image loading and caching strategies. Implemented custom UI components and animations.",
                technologies = listOf("Java", "Kotlin", "Glide", "RxJava", "Dagger 2"),
                period = "2017 - 2019",
                imageRes = null
            ),
        )
    }
    
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
        MasterDetailTopBar(
            title = "Projects",
            actions = {
                IconButton(onClick = { /* Filter */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter")
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
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
    Column {
        MasterDetailTopBar(
            title = project.title,
            showBackButton = !isLargeScreen,
            onBackClick = onBackClick,
            actions = {
                IconButton(onClick = { /* Share */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            }
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = project.role,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = project.period,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(Modifier.height(24.dp))
            
            Text(
                text = "About",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = project.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (project.imageRes != null) {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Screenshots",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(12.dp))
                Image(
                    painter = painterResource(project.imageRes),
                    contentDescription = "Project Screenshot",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Technologies",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
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
        }
    }
}
