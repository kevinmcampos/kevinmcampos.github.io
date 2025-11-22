package br.app.kevin.portfolio.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WorkHistory
import androidx.compose.ui.graphics.vector.ImageVector

enum class PortfolioDestination(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
) {
    Home("Home", Icons.Outlined.Home, Icons.Filled.Home),

    Experience("Experience", Icons.Outlined.WorkHistory, Icons.Filled.WorkHistory),

    Contact("Get in touch", Icons.Outlined.Call, Icons.Filled.Call),

    Settings("Settings", Icons.Outlined.Settings, Icons.Filled.Settings),
}
