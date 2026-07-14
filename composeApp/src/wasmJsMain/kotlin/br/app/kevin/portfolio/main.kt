package br.app.kevin.portfolio

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // render into #app, not <body>. compose puts the canvas in the container's shadow root,
    // and if that container also holds our seo text + loader as children, clicks stop reaching
    // the canvas. build the div ourselves if the page didn't ship one.
    val container = document.getElementById("app") as? HTMLElement ?: run {
        val el = document.createElement("div") as HTMLElement
        el.id = "app"
        el.style.width = "100%"
        el.style.height = "100%"
        document.body?.appendChild(el)
        el
    }
    // enable web accessibility so the UI is exposed to the browser a11y tree (screen readers,
    // and hopefully automation tools that read the accessibility tree).
    ComposeViewport(
        viewportContainer = container,
        configure = { isA11YEnabled = true },
    ) {
        App()
    }
}
