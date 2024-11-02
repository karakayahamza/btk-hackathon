package com.example.btk_hackathon.presentation.screens.search_book_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btk_hackathon.Util.Resource
import com.example.btk_hackathon.data.local.model.BookEntity
import com.example.btk_hackathon.domain.use_cases.GetGeminiBookDetailUseCase
import com.example.btk_hackathon.domain.use_cases.GetOpenLibraryBookUseCase
import com.example.btk_hackathon.domain.use_cases.InsertBookToDatabaseUseCase
import com.example.btk_hackathon.presentation.state.SaveState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val getOpenLibraryBookUseCase: GetOpenLibraryBookUseCase,
    private val getGeminiBookDetailUseCase: GetGeminiBookDetailUseCase,
    private val insertBookToDatabaseUseCase: InsertBookToDatabaseUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<SearchBookState>().apply { value = SearchBookState() }
    val state: LiveData<SearchBookState> get() = _state

    private val _saveState = MutableLiveData<SaveState>(SaveState.Idle)
    val saveState: LiveData<SaveState> get() = _saveState


    init {
        _state.value = SearchBookState()
    }


    fun resetSaveState() {
        _saveState.value = SaveState.Idle
    }

    fun fetchAndInsertBook(bookName: String, coverEditionKey: String) {
        _saveState.value = SaveState.Loading

        viewModelScope.launch {
            getGeminiBookDetailUseCase(bookName).collect { resource ->
                when (resource) {
                    is Resource.Success -> resource.data?.let { bookDetail ->
                        val bookEntity = BookEntity(
                            author = bookDetail.author,
                            genre = bookDetail.genre,
                            title = bookDetail.title,
                            authorBiography = bookDetail.authorBiography,
                            publicationDate = bookDetail.publicationDate,
                            summary = bookDetail.summary,
                            cover_edition_key = coverEditionKey,
                            main_characters = bookDetail.main_characters
                        )

                        try {
                            insertBookToDatabaseUseCase(bookEntity)
                            _saveState.value = SaveState.Success("Book saved successfully!")
                        } catch (e: Exception) {
                            _saveState.value = SaveState.Error("Failed to save the book.")
                        }
                    }

                    is Resource.Error -> {
                        _saveState.value = SaveState.Error("Failed to fetch book details.")
                    }

                    is Resource.Loading -> _saveState.value = SaveState.Loading
                }
            }
        }
    }

    fun getOpenLibraryBook(query: String) {
        viewModelScope.launch {
            _state.value = state.value?.copy(isLoading = true) ?: SearchBookState(isLoading = true)
            getOpenLibraryBookUseCase(query).collect { resource ->
                when (resource) {
                    is Resource.Success -> _state.value =
                        state.value?.copy(isLoading = false, books = resource.data)

                    is Resource.Error -> _state.value =
                        state.value?.copy(isLoading = false, error = resource.message)

                    is Resource.Loading -> _state.value = state.value?.copy(isLoading = true)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel", "BookDetailViewModel")
    }
}