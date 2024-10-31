package com.example.btk_hackathon.data.remote.repository

import android.util.Log
import com.example.btk_hackathon.data.remote.api.OpenLibraryBookApi
import com.example.btk_hackathon.data.remote.dto.GeminiBookModel
import com.example.btk_hackathon.domain.model.BookDto
import com.example.btk_hackathon.domain.model.toBookDto
import com.example.btk_hackathon.domain.repository.RemoteBookRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.gson.Gson
import javax.inject.Inject

class RemoteBookRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel,
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
        val chat = generativeModel.startChat(chatHistory)
        val response = chat.sendMessage(prompt)
        Log.d("Gemini Response", response.text.toString())

        return Gson().fromJson(response.text, GeminiBookModel::class.java)
    }
}