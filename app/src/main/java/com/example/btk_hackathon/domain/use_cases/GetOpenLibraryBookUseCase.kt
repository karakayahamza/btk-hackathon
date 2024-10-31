package com.example.btk_hackathon.domain.use_cases

import com.example.btk_hackathon.Util.Resource
import com.example.btk_hackathon.domain.model.BookDto
import com.example.btk_hackathon.domain.repository.RemoteBookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOpenLibraryBookUseCase @Inject constructor(
    private val bookRepository: RemoteBookRepository
) {
    suspend operator fun invoke(query: String): Flow<Resource<List<BookDto>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val books = bookRepository.getBookDataFromOpenLibrary(query)
                emit(Resource.Success(books))
            } catch (e: Exception) {
                emit(Resource.Error("Error fetching books: ${e.message}"))
            }
        }
    }
}