package com.example.btk_hackathon

// UIState Sealed Class for State Management
sealed class UIState<out T> {
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    object Idle : UIState<Nothing>()
}
