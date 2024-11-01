package com.example.btk_hackathon.domain.repository

import com.example.btk_hackathon.data.remote.dto.GeminiBookModel
import com.example.btk_hackathon.data.remote.dto.GeminiQuizModel
import com.example.btk_hackathon.domain.model.BookDto
import com.google.ai.client.generativeai.type.Content

interface RemoteBookRepository {
    suspend fun getBookDataFromOpenLibrary(title: String): List<BookDto>
    suspend fun getBookDataFromGemini(
        prompt: String,
        chatHistory: List<Content>
    ): GeminiBookModel
    suspend fun getQuizDataFromGemini(
        prompt: String,
        chatHistory: List<Content>
    ): GeminiQuizModel
}