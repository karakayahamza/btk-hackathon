package com.example.btk_hackathon.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.btk_hackathon.presentation.navigation.Routes
import com.example.btk_hackathon.presentation.views.home_screen.HomeScreen
import com.example.btk_hackathon.presentation.views.profile_screen.ProfileScreen
import com.example.btk_hackathon.presentation.views.settings_screen.SettingsScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit
) {
    NavHost(navController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route) {
            onBottomBarVisibilityChanged(true)
            HomeScreen()
        }
        composable(Routes.Profile.route) {
            onBottomBarVisibilityChanged(true)
            ProfileScreen()
        }
        composable(Routes.Settings.route) {
            onBottomBarVisibilityChanged(true)
            SettingsScreen()
        }
    }
}