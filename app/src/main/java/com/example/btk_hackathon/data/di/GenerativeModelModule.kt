package com.example.btk_hackathon.data.di

import com.example.btk_hackathon.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GenerativeModelModule {

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel("gemini-1.5-flash",
            BuildConfig.API_KEY,
            generationConfig = generationConfig {
                temperature = 1f
                topK = 64
                topP = 0.95f
                maxOutputTokens = 8192
                responseMimeType = "application/json"
            },
            systemInstruction = content {
                text(
                    """
                    You will be given the name of a book. Return me the following details of this book in JSON format with the specified key names:

                    - A long and detailed summary (“summary”)
                    - Author of the book (“author”)
                    - Short biography of the author (“author_biography”)
                    - Publication date of the book (“publication_date”)
                    - Type of book (“genre”)
                    - Name of the book (“book_name”)
                    - Main Characters (“main_characters”) <must be String List> 
                    
                    The JSON format should be as follows:
                    {
                        “book_name”: “Here is the name of the book”,
                        “author”: “Author name here”,
                        “author_biography”: “Author bio here”,
                        “genre”: “Book type here”,
                        “publication_date”: “Publication date here”,
                        “main_characters”: “Main Characters here”,
                        “summary”: “Here is the summary of the book”
                    }
                    """
                )
            }
        )
    }
}