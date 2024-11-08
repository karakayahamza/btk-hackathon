package com.example.btk_hackathon.presentation.screens.quiz_screen

import com.example.btk_hackathon.data.remote.dto.GeminiQuizModel

data class QuizState(
    val isLoading: Boolean = false,
    val quiz: GeminiQuizModel? = null,
    val error: String? = null
)