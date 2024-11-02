package com.example.btk_hackathon.presentation.screens.quiz_screen

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btk_hackathon.Util.Resource
import com.example.btk_hackathon.domain.use_cases.GetGeminiBookQuestions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getGeminiQuizUseCase: GetGeminiBookQuestions
) : ViewModel() {
    private val _quizUiState = MutableLiveData<QuizState>().apply { value = QuizState() }
    val quizUiState: LiveData<QuizState> get() = _quizUiState

    private val selectedAnswers = mutableStateMapOf<String, String?>()

    fun fetchQuiz(bookName: String) {
        _quizUiState.value = QuizState(isLoading = true)

        viewModelScope.launch {
            getGeminiQuizUseCase.invoke(bookName).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _quizUiState.value = QuizState(isLoading = true)
                    is Resource.Success -> resource.data?.let { quizData ->
                        _quizUiState.value = QuizState(quiz = quizData)
                    } ?: run {
                        _quizUiState.value = QuizState(error = "No quiz data available", isLoading = false)
                    }
                    is Resource.Error -> {
                        _quizUiState.value = QuizState(
                            error = resource.message ?: "Failed to fetch book details",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun getSelectedAnswer(questionText: String): String? {
        return selectedAnswers[questionText]
    }

    fun setSelectedAnswer(questionText: String, answer: String) {
        selectedAnswers[questionText] = answer
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel","QuizScreen Death")
    }
}
