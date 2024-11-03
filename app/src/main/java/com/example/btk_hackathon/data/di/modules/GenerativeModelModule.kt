package com.example.btk_hackathon.data.di.modules

import android.content.Context
import com.example.btk_hackathon.BuildConfig
import com.example.btk_hackathon.R
import com.example.btk_hackathon.data.di.qualifiers.BookDetailGenerativeModel
import com.example.btk_hackathon.data.di.qualifiers.BookQuizGenerativeModel
import com.example.btk_hackathon.data.di.qualifiers.ChatGenerativeModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

fun getUserLanguage(context: Context): String {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("language_code", "en") ?: "en"
}

@Module
@InstallIn(SingletonComponent::class)
object GenerativeModelModule {

    @BookDetailGenerativeModel
    @Provides
    @Singleton
    fun provideBookDetailGenerativeModel(@ApplicationContext context: Context): GenerativeModel {
        val systemLang = context.resources.getString(R.string.bookdetail_system_ins)
        return GenerativeModel("gemini-1.5-flash",
            BuildConfig.API_KEY,
            generationConfig = generationConfig {
                temperature = 0.5f
                topK = 64
                topP = 0.95f
                maxOutputTokens = 8192
                responseMimeType = "application/json"
            },
            systemInstruction = content {
                text(
                    systemLang
                )
            }
        )
    }

    @BookQuizGenerativeModel
    @Provides
    @Singleton
    fun provideBookQuizGenerativeModel(@ApplicationContext context: Context): GenerativeModel {
        val systemLang = context.resources.getString(R.string.quiz_system_ins)
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.API_KEY,
            generationConfig = generationConfig {
                temperature = 0.5f
                topK = 64
                topP = 0.95f
                maxOutputTokens = 8192
                responseMimeType = "application/json"
            },
            systemInstruction = content {
                text(
                    systemLang
                )
            }
        )
    }

    @ChatGenerativeModel
    @Provides
    @Singleton
    fun provideChatGenerativeModel(@ApplicationContext context: Context): GenerativeModel {
        val systemLang = context.resources.getString(R.string.chat_sysytem_ins)
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.API_KEY,
            generationConfig = generationConfig {
                temperature = 1f
                topK = 64
                topP = 0.95f
                maxOutputTokens = 8192
                responseMimeType = "text/plain"
            },

            systemInstruction = content {
                text(
                    systemLang
                )
            }
        )
    }
}