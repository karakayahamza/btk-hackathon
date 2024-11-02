package com.example.btk_hackathon.domain.use_cases

import android.util.Log
import com.example.btk_hackathon.Util.Resource
import com.example.btk_hackathon.domain.repository.RemoteBookRepository
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    private val repository: RemoteBookRepository,
) {
    suspend operator fun invoke(
        bookName: Content,
        chatHistory: List<Content>
    ): Flow<Resource<GenerateContentResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response =
                    repository.getChatMessageFromGemini(
                        bookName,
                        chatHistory
                    )
                Log.d("Response",response.text.toString())
                emit(Resource.Success(response))
            } catch (e: Exception) {
                emit(Resource.Error("Error fetching book details: ${e.message}"))
            }
        }.flowOn(Dispatchers.IO)
    }
}