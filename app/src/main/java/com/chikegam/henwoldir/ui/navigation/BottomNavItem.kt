package com.chikegam.henwoldir.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem(Screen.Home.route, Icons.Default.Home, "Home")
    object Cards : BottomNavItem(Screen.Cards.route, Icons.Default.LibraryBooks, "Cards")
    object Collection : BottomNavItem(Screen.Collection.route, Icons.Default.Star, "Collection")
    object Search : BottomNavItem(Screen.Search.route, Icons.Default.Search, "Search")
    object Settings : BottomNavItem(Screen.Settings.route, Icons.Default.Settings, "Settings")

    companion object {
        val items = listOf(Home, Cards, Collection, Search, Settings)
    }
}

