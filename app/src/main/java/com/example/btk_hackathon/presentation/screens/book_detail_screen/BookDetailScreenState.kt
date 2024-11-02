package com.example.btk_hackathon.presentation.screens.book_detail_screen

import com.example.btk_hackathon.data.local.model.BookEntity

sealed class BookDetailScreenState {
    data object Loading : BookDetailScreenState()
    data class Success(val bookEntity: BookEntity?) : BookDetailScreenState()
    data class Error(val message: String) : BookDetailScreenState()
}