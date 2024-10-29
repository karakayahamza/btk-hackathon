package com.example.btk_hackathon.domain.repository

import com.example.btk_hackathon.data.local.model.local.Book
import com.example.btk_hackathon.data.local.model.remote.GeminiBookDetailResponse
import com.example.btk_hackathon.domain.model.BookDto
import com.google.ai.client.generativeai.type.Content
import kotlinx.coroutines.flow.Flow

interface BookRepository {


    // Database
    fun getBooks(): Flow<List<Book>>
    suspend fun insert(note: Book)
    suspend fun delete(note: Book)


    //Gemini AI
    suspend fun getBookDataFromGemini(
        prompt: String,
        chatHistory: List<Content>
    ): GeminiBookDetailResponse

    // Get Book from OpenLibrary API
    suspend fun getBookDataFromOpenLibrary(title: String): List<BookDto>

}