package com.example.btk_hackathon.domain.use_cases

import com.example.btk_hackathon.Util.Resource
import com.example.btk_hackathon.data.remote.dto.GeminiBookModel
import com.example.btk_hackathon.domain.repository.RemoteBookRepository
import com.google.ai.client.generativeai.type.Content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetGeminiBookDetailUseCase @Inject constructor(
    private val repository: RemoteBookRepository
) {
    suspend operator fun invoke(bookName: String): Flow<Resource<GeminiBookModel>> {
        return flow {
            emit(Resource.Loading())
            try {
                val chatHistory = emptyList<Content>()
                val response = repository.getBookDataFromGemini(bookName, chatHistory)
                emit(Resource.Success(response))
            } catch (e: Exception) {
                emit(Resource.Error("Error fetching book details: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)
    }
}
