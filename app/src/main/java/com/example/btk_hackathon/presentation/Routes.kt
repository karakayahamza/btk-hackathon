package com.example.btk_hackathon.presentation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Profile : Screen("bookDetail/{bookTitle}/{coverEditionKey}")
    data object Settings : Screen("settings")
    data object SplashScreen : Screen("splash")
    data object OnBoardScreen : Screen("on_board")
    data object MainScreen : Screen("main")
}