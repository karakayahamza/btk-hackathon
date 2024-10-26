package com.example.btk_hackathon.presentation.navigation

import com.example.btk_hackathon.R

data class BottomNavItem(
    val route: String,
    val title: String,
    val iconResId: Int // Burada Int kullanarak resim kaynağını tutuyoruz
)

object BottomNavigationItems {
    val items = listOf(
        BottomNavItem(
            route = Routes.Home.route,
            title = "Home",
            iconResId = R.drawable.dashboard // Var olan vektör ikonun kaynak ID'si
        ),
        BottomNavItem(
            route = Routes.Profile.route,
            title = "Profile",
            iconResId = R.drawable.dashboard // Kendi ikonunuzu buraya ekleyin
        ),
        BottomNavItem(
            route = Routes.Settings.route,
            title = "Settings",
            iconResId = R.drawable.dashboard // Var olan vektör ikonun kaynak ID'si
        )
    )
}