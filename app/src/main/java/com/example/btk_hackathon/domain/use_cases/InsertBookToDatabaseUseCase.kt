package com.example.btk_hackathon.domain.use_cases

import com.example.btk_hackathon.data.local.model.BookEntity
import com.example.btk_hackathon.domain.repository.LocalBookRepository
import javax.inject.Inject

class InsertBookToDatabaseUseCase @Inject constructor(
    private val bookRepository: LocalBookRepository
) {
    suspend operator fun invoke(bookEntity: BookEntity) {
        bookRepository.insert(bookEntity)
    }
}