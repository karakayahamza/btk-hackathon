package com.example.btk_hackathon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.btk_hackathon.MainActivity
import com.example.btk_hackathon.MainScreen
import com.example.btk_hackathon.presentation.Screen
import com.example.btk_hackathon.presentation.my_library.views.MyLibraryScreen
import com.example.btk_hackathon.presentation.search_book.view.SearchBookScreen
import com.example.btk_hackathon.presentation.profile_screen.ProfileScreen
import com.example.btk_hackathon.presentation.splash_screen.SplashScreen
import com.example.btk_hackathon.presentation.welcome_screen.OnboardingScreen


@Composable
fun NavigationGraph(context: MainActivity, navController: NavHostController) {
    NavHost(navController, startDestination = Screen.SplashScreen.route) {

        composable(Screen.SplashScreen.route) {
            SplashScreen(
                navController = navController, context = context
            )
        }


//        composable(Screen.BookListScreen.route) {
//            val viewModel: BookViewModel = hiltViewModel()
//            BookListScreen(viewModel, navController)
//        }
        composable(Screen.OnBoardScreen.route) {
            OnboardingScreen(
                navController = navController, context = context
            )
        }
        composable(Screen.MainScreen.route) {
            MainScreen(context = context, navController = navController)
        }
//        composable(Screen.Profile.route) { backStackEntry ->
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
    NavHost(navController = mainNavController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            SearchBookScreen()
        }
//        composable(Screen.Profile.route) { backStackEntry ->
//            val bookTitle = backStackEntry.arguments?.getString("bookTitle")
//            val coverEditionKey = backStackEntry.arguments?.getString("coverEditionKey")
//            bookTitle?.let { coverEditionKey?.let { it1 -> ProfileScreen() } }
//        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(Screen.Settings.route) {
            MyLibraryScreen()
        }
    }
}
