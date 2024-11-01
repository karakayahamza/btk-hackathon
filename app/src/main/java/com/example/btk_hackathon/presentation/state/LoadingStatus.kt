package com.example.btk_hackathon.presentation.state

sealed class SaveState {
    data object Idle : SaveState()
    data object Loading : SaveState()
    data class Success(val message: String) : SaveState()
    data class Error(val message: String) : SaveState()
}