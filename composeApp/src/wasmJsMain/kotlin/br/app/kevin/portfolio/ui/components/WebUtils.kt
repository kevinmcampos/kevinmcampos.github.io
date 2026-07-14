package br.app.kevin.portfolio.ui.components

import kotlinx.browser.document
import kotlinx.browser.window

/** open a url in a new tab (external links, mailto). */
fun openUrl(url: String) {
    window.open(url, "_blank")
}

/** fade out the loading screen once compose is up. .hide does the css, we just leave the div there. */
fun hideLoadingScreen() {
    document.getElementById("loading")?.classList?.add("hide")
}
