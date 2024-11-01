package com.example.btk_hackathon.presentation.gemini_chat_screen

import com.google.ai.client.generativeai.type.GenerateContentResponse

data class ChatState(
    val isLoading: Boolean = false,
    val respond: GenerateContentResponse? = null,
    val error: String? = null
)
