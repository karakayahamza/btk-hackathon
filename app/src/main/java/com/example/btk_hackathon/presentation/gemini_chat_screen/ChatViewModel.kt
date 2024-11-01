package com.example.btk_hackathon.presentation.gemini_chat_screen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.btk_hackathon.Util.Resource
import com.example.btk_hackathon.domain.use_cases.ChatUseCase
import com.google.ai.client.generativeai.type.Content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase,
    application: Application
) : AndroidViewModel(application = application) {

    private val _chatHistory = MutableStateFlow<List<Content>>(emptyList())
    val chatHistory: StateFlow<List<Content>> = _chatHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _responseState = MutableLiveData<ChatState>().apply { value = ChatState() }
    val responseState: LiveData<ChatState> get() = _responseState


    private fun addMessage(content: Content) {
        val updatedMessages = _chatHistory.value + content
        _chatHistory.value = updatedMessages
    }

    fun sendMessageToChat(inputMessage: Content) {
        addMessage(inputMessage)
        setLoadingState(true)

        viewModelScope.launch(Dispatchers.IO) {
            chatUseCase.invoke(inputMessage, _chatHistory.value).collect { respond ->
                when (respond) {
                    is Resource.Loading -> {
                        withContext(Dispatchers.Main) {
                            _responseState.value = ChatState(isLoading = true)
                        }
                    }

                    is Resource.Success -> respond.data?.let { quizData ->
                        withContext(Dispatchers.Main) {
                            _responseState.value = ChatState(respond = quizData)

                            val responseContent = Content.Builder().apply {
                                role = "model"
                                text(quizData.text ?: "Yanıt boş")
                            }.build()

                            Log.d("Response",quizData.text.toString())

                            addMessage(responseContent)
                            setLoadingState(false)
                        }
                    } ?: run {
                        withContext(Dispatchers.Main) {
                            _responseState.value =
                                ChatState(error = "No quiz data available", isLoading = false)
                        }
                    }

                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            _responseState.value = ChatState(
                                error = respond.message ?: "Failed to fetch book details",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setLoadingState(loading: Boolean) {
        _isLoading.value = loading
    }
}