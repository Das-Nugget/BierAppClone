package com.example.beer.data.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.SportsBar
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class TabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Beer(
        title = "Bier",
        selectedIcon = Icons.Filled.SportsBar,
        unselectedIcon = Icons.Outlined.SportsBar
    ),
    Rating(
        title = "Rating",
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.StarOutline
    ),
    Settings(
        title = "Einstellungen",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}