package com.example.btk_hackathon.data.di.modules

import com.example.btk_hackathon.BuildConfig
import com.example.btk_hackathon.data.di.qualifiers.BookDetailGenerativeModel
import com.example.btk_hackathon.data.di.qualifiers.BookQuizGenerativeModel
import com.example.btk_hackathon.data.di.qualifiers.ChatGenerativeModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import java.util.Locale

fun getUserLanguage(): String {
    return Locale.getDefault().language
}

@Module
@InstallIn(SingletonComponent::class)
object GenerativeModelModule {

    @BookDetailGenerativeModel
    @Provides
    @Singleton
    fun provideBookDetailGenerativeModel(): GenerativeModel {
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

    @BookQuizGenerativeModel
    @Provides
    @Singleton
    fun provideBookQuizGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.API_KEY,
            generationConfig = generationConfig {
                temperature = 1f
                topK = 64
                topP = 0.95f
                maxOutputTokens = 8192
                responseMimeType = "application/json"
            },
            systemInstruction = content {
                text(
                    "I want you to take the title of the book you have been given and write 10 questions about it. \n" +
                            "The questions should have 4 options.\n" +
                            "You must also send me the correct answer.\n" +
                            "Explain the correct answers but it must properly explain the correct answer.\n" +
                            "Do not ask the same questions as before. " +
                            "{\n" +
                            "  \"questions\": [\n" +
                            "    {\n" +
                            "      \"answers\": [\"String\", \"String\", \"String\", \"String\"],\n" +
                            "      \"correct_answer\": \"String\",\n" +
                            "      \"explanation\": \"String\",\n" +
                            "      \"question\": \"String\"\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}"
                )
            }
        )
    }

    @ChatGenerativeModel
    @Provides
    @Singleton
    fun provideChatGenerativeModel(): GenerativeModel {
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
                    """
                        You will be given the name of a book and language of the user.
                        You have to take and answer questions of the user regarding this book.
                        If user input doesn't relate to book write a simple refusal that states you can’t talk about matters other than asked book.
                    """.trimIndent()+
                    "User's language code for this instance:" + getUserLanguage() +
                    "Please start conversation in this language."
                )
            }
        )
    }
}