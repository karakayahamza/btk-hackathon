package com.example.btk_hackathon.presentation.screens.my_library_screen

import com.example.btk_hackathon.data.local.model.BookEntity

sealed class MyLibraryUiState {
    data object Loading : MyLibraryUiState()
    data class Success(val bookEntities: List<BookEntity>) : MyLibraryUiState()
    data class Error(val message: String) : MyLibraryUiState()
}