package com.example.btk_hackathon

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val languageCode = loadLanguagePreference(this)
        setLocale(this, languageCode)
    }
}

fun setLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = context.resources.configuration
    config.setLocale(locale)

    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}


fun loadLanguagePreference(context: Context): String {
    val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return sharedPref.getString("language_code", "en") ?: "en"
}