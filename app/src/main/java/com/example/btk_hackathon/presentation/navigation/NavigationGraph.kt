package com.example.btk_hackathon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.btk_hackathon.BookListScreen
import com.example.btk_hackathon.MainActivity
import com.example.btk_hackathon.MainScreen
import com.example.btk_hackathon.presentation.views.BookViewModel
import com.example.btk_hackathon.presentation.views.SplashScreen
import com.example.btk_hackathon.presentation.views.home_screen.HomeScreen
import com.example.btk_hackathon.presentation.views.profile_screen.ProfileScreen
import com.example.btk_hackathon.presentation.views.settings_screen.SettingsScreen
import com.example.btk_hackathon.presentation.views.welcome_screen.OnboardingScreen


@Composable
fun NavigationGraph(context: MainActivity, navController: NavHostController) {
    NavHost(navController, startDestination = Routes.SplashScreen.route) {

        composable(Routes.SplashScreen.route){
            SplashScreen(
                navController = navController,
                context = context
            )
        }


        composable(Routes.BookListScreen.route) {
            val viewModel: BookViewModel = hiltViewModel()
            BookListScreen(viewModel, navController)
        }
        composable(Routes.OnBoardScreen.route) {
            OnboardingScreen(
                navController = navController,
                context = context
            )
        }
        composable(Routes.MainScreen.route) {
            MainScreen(context = context, navController = navController)
        }
//        composable(Routes.Profile.route) { backStackEntry ->
//            val bookTitle = backStackEntry.arguments?.getString("bookTitle")
//            val coverEditionKey = backStackEntry.arguments?.getString("coverEditionKey")
//            if (bookTitle != null && coverEditionKey != null) {
//                ProfileScreen(bookTitle, coverEditionKey)
//            }
//        }
    }
}

@Composable
fun MainScreenNavHost(mainNavController: NavHostController) {
    NavHost(navController = mainNavController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route) {
            HomeScreen()
        }
//        composable(Routes.Profile.route) { backStackEntry ->
//            val bookTitle = backStackEntry.arguments?.getString("bookTitle")
//            val coverEditionKey = backStackEntry.arguments?.getString("coverEditionKey")
//            bookTitle?.let { coverEditionKey?.let { it1 -> ProfileScreen() } }
//        }

        composable(Routes.Profile.route) {
            ProfileScreen()
        }
        composable(Routes.Settings.route) {
            SettingsScreen()
        }
    }
}
