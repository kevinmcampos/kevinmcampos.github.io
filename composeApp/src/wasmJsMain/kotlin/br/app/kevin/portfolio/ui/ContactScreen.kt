package br.app.kevin.portfolio.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import br.app.kevin.portfolio.ContactInfo
import br.app.kevin.portfolio.ui.components.openUrl

// tab / shift+tab focus the given field directly. compose's implicit traversal skips the
// multi-line message field, so we requestFocus the target instead of relying on moveFocus.
private fun Modifier.tabTo(next: FocusRequester? = null, previous: FocusRequester? = null): Modifier =
    onPreviewKeyEvent { e ->
        if (e.type == KeyEventType.KeyDown && e.key == Key.Tab) {
            val target = if (e.isShiftPressed) previous else next
            target?.requestFocus()
            target != null
        } else {
            false
        }
    }

@Composable
fun ContactScreen() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val messageFocus = remember { FocusRequester() }
    val sendFocus = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Get in Touch",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Have a project in mind? Let's talk.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(nameFocus)
                        .tabTo(next = emailFocus)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(emailFocus)
                        .tabTo(next = messageFocus, previous = nameFocus)
                )

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Message") },
                    minLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(messageFocus)
                        .tabTo(next = sendFocus, previous = emailFocus)
                )

                Button(
                    onClick = {
                        openUrl(
                            ContactInfo.mailto(
                                subject = "Portfolio contact${if (name.isNotBlank()) " from $name" else ""}",
                                body = buildString {
                                    if (message.isNotBlank()) appendLine(message).appendLine()
                                    if (name.isNotBlank() || email.isNotBlank()) {
                                        append("— ")
                                        append(name.ifBlank { "someone" })
                                        if (email.isNotBlank()) append(" ($email)")
                                    }
                                },
                            )
                        )
                    },
                    modifier = Modifier.align(Alignment.End).focusRequester(sendFocus).tabTo(previous = messageFocus)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Send Message")
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            "Prefer a direct line?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { openUrl(ContactInfo.mailto()) }) { Text("Email") }
            TextButton(onClick = { openUrl(ContactInfo.GITHUB) }) { Text("GitHub") }
            TextButton(onClick = { openUrl(ContactInfo.LINKEDIN) }) { Text("LinkedIn") }
        }
    }
}
