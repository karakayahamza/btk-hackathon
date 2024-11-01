package com.example.btk_hackathon.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Profile : Screen("bookDetail/{bookTitle}/{coverEditionKey}")
    data object MyLibraryScreen : Screen("my_library")
    data object SplashScreen : Screen("splash")
    data object OnBoardScreen : Screen("on_board")
    data object MainScreen : Screen("main")
    data object BookInfoScreen : Screen("book_detail/{bookID}")
    data object BookDetailScreen : Screen("book_detail/{bookID}") {
        fun createRoute(bookID: String) = "book_detail/$bookID"
    }

    data object QuizScreen : Screen("quiz/{book_name}") {
        fun createRoute(bookName: String) = "quiz/$bookName"
    }

    data object GeminiChatScreen : Screen("gemini_chat/{book_name}") {
        fun createRoute(bookName: String) = "gemini_chat/$bookName"
    }
}