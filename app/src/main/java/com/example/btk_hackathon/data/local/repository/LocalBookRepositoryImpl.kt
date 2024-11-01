package com.example.btk_hackathon.data.local.repository

import com.example.btk_hackathon.data.local.dao.BookDao
import com.example.btk_hackathon.data.local.model.BookEntity
import com.example.btk_hackathon.domain.repository.LocalBookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalBookRepositoryImpl @Inject constructor(
    private val bookDao: BookDao
) : LocalBookRepository {

    override fun getBooks(): Flow<List<BookEntity>> {
        return bookDao.getAllBooks()
    }

    override fun getBooksById(bookId: String): Flow<BookEntity?> {
        return bookDao.getBookById(bookId = bookId)
    }

    override suspend fun insert(bookEntity: BookEntity) {
        bookDao.insertBook(bookEntity)
    }

    override suspend fun delete(bookEntity: BookEntity) {
        bookDao.delete(bookEntity)
    }
}