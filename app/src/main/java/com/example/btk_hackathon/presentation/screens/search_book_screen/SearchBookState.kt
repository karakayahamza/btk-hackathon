package com.example.btk_hackathon.presentation.screens.search_book_screen

import com.example.btk_hackathon.domain.model.BookDto

data class SearchBookState(
    val isLoading: Boolean = false,
    val books: List<BookDto>? = null,
    val error: String? = null
)
