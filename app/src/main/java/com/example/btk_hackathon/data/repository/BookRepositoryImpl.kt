package com.example.btk_hackathon.data.repository

import android.util.Log
import com.example.btk_hackathon.data.local.database.BookDao
import com.example.btk_hackathon.data.local.model.local.Book
import com.example.btk_hackathon.data.local.model.remote.GeminiBookDetailResponse
import com.example.btk_hackathon.data.local.model.remote.service.OpenLibraryBookApi
import com.example.btk_hackathon.domain.model.BookDto
import com.example.btk_hackathon.domain.model.toBookDto
import com.example.btk_hackathon.domain.repository.BookRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val openLibraryBookApi: OpenLibraryBookApi,
    private val api: BookDao
) : BookRepository {


    // Dao Database
    override fun getBooks(): Flow<List<Book>> {
        return api.getAllBooks()
    }

    override suspend fun insert(book: Book) {
        Log.d("Book", book.author)
        Log.d("Book", book.genre)
        Log.d("Book", book.publicationDate)
        api.insertBook(book)
    }

    override suspend fun delete(book: Book) {
        Log.d("Book", book.author)
        Log.d("Book", book.genre)
        Log.d("Book", book.publicationDate)
        api.delete(book)
    }

    // Open Library
    override suspend fun getBookDataFromOpenLibrary(title: String): List<BookDto> {
        val response = openLibraryBookApi.getData(title)

        Log.d("API_RESPONSE", response.toString())

        return response.docs.map { it.toBookDto() }
    }

    override suspend fun getBookDataFromGemini(
        prompt: String,
        chatHistory: List<Content>
    ): GeminiBookDetailResponse {
        try {
            val chat = generativeModel.startChat(chatHistory)
            val response = chat.sendMessage(prompt)

            // Log the raw response
            response.text?.let { Log.d("TEST1", it) }

            // Parse the response into the expected data structure
            val bookResponse = Gson().fromJson(response.text, GeminiBookDetailResponse::class.java)

            // Log the book response fields
            Log.d("TEST_AUTHOR", bookResponse.author ?: "Author is null")
            Log.d("TEST_TITLE", bookResponse.title ?: "Author is null")
            Log.d("TEST_GENRE", bookResponse.genre ?: "Genre is null")
            Log.d("TEST_PUBLICATION_DATE", bookResponse.publicationDate ?: "Publication Date is null")
            Log.d("TEST_SUMMARY", bookResponse.summary ?: "Summary is null")

            return bookResponse
        } catch (e: Exception) {
            // Log the exception to understand what went wrong
            Log.e("ERROR", "Failed to get book data: ${e.message}", e)
            throw e // Rethrow the exception if you want to handle it upstream
        }
    }
}