package com.example.btk_hackathon.domain.repository

import com.example.btk_hackathon.data.local.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getBooks(): Flow<List<Book>>
    suspend fun insert(note: Book)
    suspend fun delete(note: Book)
}