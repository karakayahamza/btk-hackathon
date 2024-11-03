package com.example.btk_hackathon.presentation.screens.my_library_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btk_hackathon.data.local.model.BookEntity
import com.example.btk_hackathon.domain.repository.LocalBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyLibraryViewModel @Inject constructor(private val bookRepository: LocalBookRepository) : ViewModel() {

    private val _myLibraryUiState = MutableStateFlow<MyLibraryUiState>(MyLibraryUiState.Loading)
    val myLibraryUiState: StateFlow<MyLibraryUiState> = _myLibraryUiState.asStateFlow()

    // This holds the list of books
    private val _booksState = MutableStateFlow<List<BookEntity>>(emptyList())
    val booksState: StateFlow<List<BookEntity>> = _booksState.asStateFlow()

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        viewModelScope.launch {
            bookRepository.getBooks()
                .onStart { _myLibraryUiState.value = MyLibraryUiState.Loading }
                .catch { e ->
                    _myLibraryUiState.value = MyLibraryUiState.Error(e.message ?: "Unknown error")
                }
                .collect { data ->
                    _myLibraryUiState.value = MyLibraryUiState.Success(data)
                    _booksState.value = data // Update the books state
                }
        }
    }

    fun deleteBook(book: BookEntity) {
        viewModelScope.launch {
            Log.d("Viewmodel MyLibrary", "Deleting: ${book.title}") // Assuming BookEntity has a title property
            bookRepository.delete(book)

            _booksState.update { currentList ->
                currentList.filter { it.id != book.id } // Assuming BookEntity has an id property
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel", "MyLibraryViewModel cleared")
    }
}
