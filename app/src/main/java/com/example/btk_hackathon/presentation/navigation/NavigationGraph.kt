package com.example.btk_hackathon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.btk_hackathon.MainActivity
import com.example.btk_hackathon.MainScreen
import com.example.btk_hackathon.data.local.model.BookEntity
import com.example.btk_hackathon.presentation.book_detail_screen.views.BookDetailScreen
import com.example.btk_hackathon.presentation.book_detail_screen.views.BookInfoScreen
import com.example.btk_hackathon.presentation.gemini_chat_screen.GeminiChatScreen
import com.example.btk_hackathon.presentation.my_library.views.MyLibraryScreen
import com.example.btk_hackathon.presentation.profile_screen.ProfileScreen
import com.example.btk_hackathon.presentation.quiz_screen.views.QuizScreen
import com.example.btk_hackathon.presentation.search_book.view.SearchBookScreen
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
        composable(Screen.OnBoardScreen.route) {
            OnboardingScreen(
                navController = navController, context = context
            )
        }
        composable(Screen.MainScreen.route) {
            MainScreen()
        }
    }
}

@Composable
fun MainScreenNavHost(mainNavController: NavHostController) {
    NavHost(navController = mainNavController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            SearchBookScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(Screen.MyLibraryScreen.route) {
            MyLibraryScreen(mainNavController)
        }
        composable(Screen.BookDetailScreen.route) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookID")
            BookDetailScreen(bookId ?: "No ID")
        }
    }
}

@Composable
fun BookDetailNavHost(bookNavController: NavHostController, book: BookEntity) {
    NavHost(navController = bookNavController, startDestination = Screen.BookInfoScreen.route) {
        composable(Screen.BookInfoScreen.route) {
            BookInfoScreen(bookNavController = bookNavController, book)
        }

        composable(Screen.QuizScreen.route) { backStackEntry ->
            val bookName = backStackEntry.arguments?.getString("book_name")
            QuizScreen(bookNavController, bookName ?: "defaultName")
        }

        composable(Screen.GeminiChatScreen.route) { backStackEntry ->
            val bookName = backStackEntry.arguments?.getString("book_name")
            GeminiChatScreen(bookNavController,bookName ?: "defaultName")
        }
    }
}