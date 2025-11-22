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
import br.app.kevin.portfolio.ui.components.AdaptiveMasterDetailScaffold
import br.app.kevin.portfolio.ui.components.MasterDetailTopBar
import br.app.kevin.portfolio.ui.components.MasterListItem

// Data model example
data class ExperienceItem(
    val id: String,
    val sender: String,
    val subject: String,
    val preview: String,
    val content: String,
    val timestamp: String,
    val isRead: Boolean = false
)

@Composable
fun ExperienceScreen() {
    val experiences = remember {
        listOf(
            ExperienceItem("1", "Alice Johnson", "Project Update", "The quarterly report is ready...", "Full email content here...", "10:30 AM"),
            ExperienceItem("2", "Bob Smith", "Meeting Tomorrow", "Don't forget about our meeting...", "Full meeting details...", "9:15 AM"),
            ExperienceItem("3", "Carol Davis", "Weekend Plans", "Are you free this weekend?", "Let's plan something fun...", "Yesterday"),
        )
    }
    
    var selectedExperience by remember { mutableStateOf<ExperienceItem?>(null) }

    AdaptiveMasterDetailScaffold(
        items = experiences,
        selectedItem = selectedExperience,
        onItemSelected = { email -> selectedExperience = email },
        onBackPressed = { selectedExperience = null },
        masterPane = { items, selected, onItemClick, isLargeScreen ->
            ExperienceList(
                emails = items,
                selectedEmail = selected,
                onEmailClick = onItemClick,
                isLargeScreen = isLargeScreen
            )
        },
        detailPane = { email, onBack, isLargeScreen ->
            ExperienceDetail(
                email = email,
                onBackClick = onBack,
                isLargeScreen = isLargeScreen
            )
        }
    )
}

@Composable
private fun ExperienceList(
    emails: List<ExperienceItem>,
    selectedEmail: ExperienceItem?,
    onEmailClick: (ExperienceItem) -> Unit,
    isLargeScreen: Boolean
) {
    Column {
        MasterDetailTopBar(
            title = "Inbox (${emails.size})",
            actions = {
                IconButton(onClick = { /* Add search */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = { /* Add more options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(emails, key = { it.id }) { email ->
                EmailListItem(
                    email = email,
                    selected = selectedEmail?.id == email.id && isLargeScreen,
                    onClick = { onEmailClick(email) }
                )
            }
        }
    }
}

@Composable
private fun EmailListItem(
    email: ExperienceItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    MasterListItem(
        title = email.subject,
        subtitle = "${email.sender} • ${email.timestamp}",
        selected = selected,
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = if (email.isRead) Icons.Default.MailOutline else Icons.Default.Mail,
                contentDescription = if (email.isRead) "Read" else "Unread",
                tint = if (email.isRead)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Composable
private fun ExperienceDetail(
    email: ExperienceItem,
    onBackClick: () -> Unit,
    isLargeScreen: Boolean
) {
    Column {
        MasterDetailTopBar(
            title = "Email",
            showBackButton = !isLargeScreen,
            onBackClick = onBackClick,
            actions = {
                IconButton(onClick = { /* Reply */ }) {
                    Icon(Icons.Default.Reply, contentDescription = "Reply")
                }
                IconButton(onClick = { /* Archive */ }) {
                    Icon(Icons.Default.Archive, contentDescription = "Archive")
                }
                IconButton(onClick = { /* Delete */ }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Email header
            Text(
                text = email.subject,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "From: ${email.sender}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = email.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            // Email content
            Text(
                text = email.content,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
