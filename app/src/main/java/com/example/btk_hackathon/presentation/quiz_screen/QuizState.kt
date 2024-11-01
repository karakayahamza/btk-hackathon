package com.example.btk_hackathon.presentation.quiz_screen

import com.example.btk_hackathon.data.remote.dto.GeminiQuizModel
import com.example.btk_hackathon.domain.model.BookDto

data class QuizState(
    val isLoading: Boolean = false,
    val quiz: GeminiQuizModel? = null,
    val error: String? = null
)