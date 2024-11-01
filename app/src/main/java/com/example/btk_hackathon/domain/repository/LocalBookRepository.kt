package com.example.btk_hackathon.domain.repository

import com.example.btk_hackathon.data.local.model.BookEntity
import kotlinx.coroutines.flow.Flow

interface LocalBookRepository {
    fun getBooks(): Flow<List<BookEntity>>
    fun getBooksById(bookId:String): Flow<BookEntity?>
    suspend fun insert(bookEntity: BookEntity)
    suspend fun delete(bookEntity: BookEntity)
}