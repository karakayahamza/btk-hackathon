package com.example.btk_hackathon

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.btk_hackathon.Util.LocaleUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("LIFECYCLE", "Application created")
        val languageCode = loadLanguagePreference(this)
        Log.d("LIFECYCLE", "Loaded language code: $languageCode")
        LocaleUtils.setLocale(this, languageCode)
    }
}

fun loadLanguagePreference(context: Context): String {
    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return sharedPref.getString("language_code", "en") ?: "en"
}