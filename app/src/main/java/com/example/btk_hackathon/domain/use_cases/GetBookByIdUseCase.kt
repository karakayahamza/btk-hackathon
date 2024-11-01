package com.example.btk_hackathon.domain.use_cases

import com.example.btk_hackathon.data.local.model.BookEntity
import com.example.btk_hackathon.domain.repository.LocalBookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookByIdUseCase @Inject constructor(
    private val bookRepository: LocalBookRepository
) {
    operator fun invoke(bookID: String): Flow<BookEntity?> {
        return bookRepository.getBooksById(bookID)
    }
}