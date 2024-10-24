package com.example.btk_hackathon.presentation.navigation

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Profile : Routes("profile")
    data object Settings : Routes("settings")
}