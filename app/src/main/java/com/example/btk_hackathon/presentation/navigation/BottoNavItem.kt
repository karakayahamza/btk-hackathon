package com.example.btk_hackathon.presentation.navigation

import com.example.btk_hackathon.R
import com.example.btk_hackathon.presentation.Screen

data class BottomNavItem(
    val route: String,
    val title: String,
    val iconResId: Int
)

object BottomNavigationItems {
    val items = listOf(
        BottomNavItem(
            route = Screen.Settings.route,
            title = "My Library",
            iconResId = R.drawable.library,
        ),
        BottomNavItem(
            route = Screen.Home.route,
            title = "Search",
            iconResId = R.drawable.search_books
        ),
        BottomNavItem(
            route = Screen.Profile.route,
            title = "Profile",
            iconResId = R.drawable.dashboard
        ),
    )
}