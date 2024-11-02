package com.example.btk_hackathon.presentation.screens.book_detail_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btk_hackathon.domain.use_cases.GetBookByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val getBookByIdUseCase: GetBookByIdUseCase
) : ViewModel() {

    private val _bookDetailScreenState = MutableStateFlow<BookDetailScreenState>(
        BookDetailScreenState.Loading
    )
    val bookDetailScreenState: StateFlow<BookDetailScreenState> =
        _bookDetailScreenState.asStateFlow()

    fun fetchBookById(bookID: String) {
        viewModelScope.launch {
            getBookByIdUseCase(bookID)
                .onStart { _bookDetailScreenState.value = BookDetailScreenState.Loading }
                .catch { e ->
                    _bookDetailScreenState.value =
                        BookDetailScreenState.Error(e.message ?: "Unknown error")
                }
                .collect { bookEntity ->
                    _bookDetailScreenState.value = if (bookEntity != null) {
                        BookDetailScreenState.Success(bookEntity)
                    } else {
                        BookDetailScreenState.Error("Book not found")
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel", "BookDetailViewModel Death")
    }
}