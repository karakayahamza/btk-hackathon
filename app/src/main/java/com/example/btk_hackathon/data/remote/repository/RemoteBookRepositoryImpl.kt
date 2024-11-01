package com.example.btk_hackathon.data.remote.repository

import android.util.Log
import com.example.btk_hackathon.data.remote.api.OpenLibraryBookApi
import com.example.btk_hackathon.data.remote.dto.GeminiBookModel
import com.example.btk_hackathon.data.remote.dto.GeminiQuizModel
import com.example.btk_hackathon.domain.model.BookDto
import com.example.btk_hackathon.domain.model.toBookDto
import com.example.btk_hackathon.domain.repository.RemoteBookRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.gson.Gson
import javax.inject.Inject

class RemoteBookRepositoryImpl @Inject constructor(
    private val bookGenerativeModel: GenerativeModel,
    private val quizGenerativeModel: GenerativeModel,
    private val chatGenerativeModel: GenerativeModel,
    private val openLibraryBookApi: OpenLibraryBookApi,
) : RemoteBookRepository {
    override suspend fun getBookDataFromOpenLibrary(title: String): List<BookDto> {
        val response = openLibraryBookApi.getData(title)
        return response.docs.map { it.toBookDto() }
    }

    override suspend fun getBookDataFromGemini(
        prompt: String,
        chatHistory: List<Content>
    ): GeminiBookModel {

        val chat = bookGenerativeModel.startChat(chatHistory)
        val response = chat.sendMessage(prompt)
        Log.d("Gemini Response", response.text.toString())

        return Gson().fromJson(response.text, GeminiBookModel::class.java)
    }

    override suspend fun getQuizDataFromGemini(
        prompt: String,
        chatHistory: List<Content>
    ): GeminiQuizModel {
        val chat = quizGenerativeModel.startChat(chatHistory)
        val response = chat.sendMessage("Book: $prompt")
        Log.d("Questions", response.text.toString())
        return Gson().fromJson(response.text, GeminiQuizModel::class.java)
    }

    override suspend fun getChatMessageFromGemini(
        prompt: Content,
        chatHistory: List<Content>
    ): GenerateContentResponse {
        val chat = chatGenerativeModel.startChat(chatHistory)
        val response = chat.sendMessage(prompt)
        Log.d("Response",response.text.toString())
        return response
    }
}