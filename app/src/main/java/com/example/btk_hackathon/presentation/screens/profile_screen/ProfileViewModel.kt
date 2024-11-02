package com.example.btk_hackathon.presentation.screens.profile_screen

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.btk_hackathon.MainActivity
import com.example.btk_hackathon.setLocale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val _isEnglish = MutableStateFlow(isEnglishLanguage())
    val isEnglish: StateFlow<Boolean> = _isEnglish

    fun toggleLanguage(context: Context) {
        val newLanguageIsEnglish = !_isEnglish.value
        Log.d("ProfileViewModel", "Changing language to: ${if (newLanguageIsEnglish) "en" else "tr"}")
        _isEnglish.value = newLanguageIsEnglish
        saveLanguagePreference(newLanguageIsEnglish)
        setLocale(context, if (newLanguageIsEnglish) "en" else "tr")
        restartActivity(context)
    }

    private fun saveLanguagePreference(isEnglish: Boolean) {
        sharedPreferences.edit().putString("language_code", if (isEnglish) "en" else "tr").apply()
    }

    private fun isEnglishLanguage(): Boolean {
        return sharedPreferences.getString("language_code", "en") == "en"
    }

    private fun restartActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}