package com.example.btk_hackathon.data.repository

import android.util.Log
import com.example.btk_hackathon.data.local.database.BookDao
import com.example.btk_hackathon.data.local.model.Book
import com.example.btk_hackathon.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val api: BookDao) : BookRepository {
    override fun getBooks(): Flow<List<Book>> {
        return api.getAllBooks()
    }

    override suspend fun insert(book: Book) {
        Log.d("Book", book.yazar)
        api.insertBook(book)
    }

    override suspend fun delete(book: Book) {
        api.delete(book)
    }


}