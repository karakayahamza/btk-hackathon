package com.example.btk_hackathon.data.remote.dto

data class Question(
    val answers: List<String>,
    val correct_answer: String,
    val explanation: String,
    val question: String
)