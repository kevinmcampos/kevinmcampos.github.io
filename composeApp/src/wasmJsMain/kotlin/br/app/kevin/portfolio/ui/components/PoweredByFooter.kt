package br.app.kevin.portfolio.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp

private const val KOTLIN_MULTIPLATFORM_URL = "https://kotlinlang.org/docs/multiplatform.html"
private const val COMPOSE_MULTIPLATFORM_URL = "https://github.com/JetBrains/compose-multiplatform"

@Composable
fun PoweredByFooter(modifier: Modifier = Modifier) {
    val linkStyles = TextLinkStyles(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
        )
    )

    val text = buildAnnotatedString {
        append("Powered by ")
        withLink(LinkAnnotation.Url(KOTLIN_MULTIPLATFORM_URL, styles = linkStyles)) {
            append("Kotlin Multiplatform")
        }
        append(" · Built for web with ")
        withLink(LinkAnnotation.Url(COMPOSE_MULTIPLATFORM_URL, styles = linkStyles)) {
            append("Compose Multiplatform")
        }
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    )
}
