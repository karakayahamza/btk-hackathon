package com.example.btk_hackathon.presentation.components

import com.example.btk_hackathon.R
import com.example.btk_hackathon.presentation.navigation.Screen

data class BottomNavItem(
    val route: String,
    val title: Int,
    val iconResId: Int
)

object BottomNavigationItems {
    val items = listOf(
        BottomNavItem(
            route = Screen.MyLibraryScreen.route,
            title = R.string.my_library,
            iconResId = R.drawable.library,
        ),
        BottomNavItem(
            route = Screen.Home.route,
            title = R.string.search_book,
            iconResId = R.drawable.search_books
        ),
        BottomNavItem(
            route = Screen.Profile.route,
            title = R.string.profile,
            iconResId = R.drawable.settings
        ),
    )
}