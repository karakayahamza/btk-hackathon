package com.example.btk_hackathon.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

object BottomNavigationItems {
    val items = listOf(
        BottomNavItem(
            route = Routes.Home.route,
            title = "Home",
            icon = Icons.Outlined.Home
        ),
        BottomNavItem(
            route = Routes.Profile.route,
            title = "Profile",
            icon = Icons.Outlined.AddCircle
        ),
        BottomNavItem(
            route = Routes.Settings.route,
            title = "Settings",
            icon = Icons.Outlined.AccountCircle
        )
    )
}