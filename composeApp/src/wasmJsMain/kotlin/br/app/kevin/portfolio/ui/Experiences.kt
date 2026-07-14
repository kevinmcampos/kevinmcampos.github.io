package br.app.kevin.portfolio.ui

import br.app.kevin.portfolio.ui.components.ProjectShot

/**
 * A single portfolio project.
 *
 * Pure data — no `@Composable`, no Compose state — so it can be asserted in tests without a
 * renderer (see `wasmJsTest/.../ui/ExperiencesTest.kt`). The UI that renders these lives in
 * [ExperienceScreen]. Keep this file free of Compose UI so the data stays part of the
 * machine-verifiable core.
 */
data class ProjectItem(
    val id: String,
    val title: String,
    val role: String,
    val description: String,
    val technologies: List<String>,
    val period: String,
    val link: String? = null,
    // Placeholder captions today; give each a real image via ProjectShot(image = Res.drawable.x).
    val screenshots: List<ProjectShot> = emptyList(),
)

// real experience, newest first
internal val experiences = listOf(
    ProjectItem(
        id = "nbcu",
        title = "NBCUniversal — Authentication SDK",
        role = "Android Engineer (Contract)",
        description = "The authentication SDK powering login across 30+ NBCUniversal apps " +
            "(The Voice, Telemundo, NBC News). One shared Kotlin Multiplatform codebase targets " +
            "Android, iOS, tvOS, and web. I led the XML → Compose Multiplatform migration and " +
            "raised UI coverage with Paparazzi snapshot testing.",
        technologies = listOf("Kotlin", "Kotlin Multiplatform", "Compose Multiplatform", "Ktor", "Koin", "Coroutines"),
        period = "2023 - Present",
        link = null,
        screenshots = listOf(
            ProjectShot("Sign in"),
            ProjectShot("Profiles"),
            ProjectShot("tvOS login"),
            ProjectShot("Web SDK"),
        ),
    ),
    ProjectItem(
        id = "gocity",
        title = "Go City® — Travel Apps",
        role = "Android Engineer",
        description = "Apps for the travel industry, including The London Pass® and The New York " +
            "Pass®. I led a greenfield relaunch of the Android app from scratch with modern " +
            "architecture and Jetpack Compose, keeping feature parity with the legacy app and " +
            "handling localization across cities.",
        technologies = listOf("Kotlin", "Jetpack Compose", "Coroutines", "MVVM", "Hilt", "Retrofit"),
        period = "2021 - 2022",
        link = null,
        screenshots = listOf(
            ProjectShot("Home"),
            ProjectShot("Passes"),
            ProjectShot("Map"),
            ProjectShot("Wallet"),
        ),
    ),
    ProjectItem(
        id = "argos",
        title = "Argos — Shopping App",
        role = "Android Engineer",
        description = "The Argos shopping app, a UK e-commerce platform with over 1M monthly " +
            "active users held at a 99.9% crash-free rate. I migrated legacy Java to a modern " +
            "architecture and improved checkout flows alongside product, UX, and data teams.",
        technologies = listOf("Kotlin", "Java", "RxJava", "Coroutines", "MVVM", "Dagger"),
        period = "2019 - 2021",
        link = null,
        screenshots = listOf(
            ProjectShot("Home"),
            ProjectShot("Search"),
            ProjectShot("Product"),
            ProjectShot("Checkout"),
        ),
    ),
    ProjectItem(
        id = "huru",
        title = "Huru Systems — Supply Chain",
        role = "Android Engineer",
        description = "Led development of a supply-chain app that scans encrypted QR/barcodes and " +
            "tracks asset geolocation. Defined the architecture and patterns, documented decisions, " +
            "and ran code reviews.",
        technologies = listOf("Kotlin", "MVVM", "Android SDK"),
        period = "2018 - 2019",
        link = null,
        screenshots = listOf(
            ProjectShot("Scanner"),
            ProjectShot("Assets"),
            ProjectShot("Map"),
        ),
    ),
    ProjectItem(
        id = "nkey",
        title = "nKey — Mobile Apps",
        role = "iOS & Android Engineer",
        description = "Built native iOS apps (Objective-C, Swift), native Android apps (Java), and " +
            "hybrid React Native apps. Had the autonomy to analyze, design, estimate, and ship " +
            "production apps from scratch across both platforms.",
        technologies = listOf("Swift", "Objective-C", "Java", "Kotlin", "React Native"),
        period = "2015 - 2017",
        link = null,
        screenshots = listOf(
            ProjectShot("Onboarding"),
            ProjectShot("Home"),
            ProjectShot("Settings"),
        ),
    ),
)
