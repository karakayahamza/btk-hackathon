package com.example.btk_hackathon.data.di

import android.content.Context
import androidx.room.Room
import com.example.btk_hackathon.BuildConfig
import com.example.btk_hackathon.Util.Util.BASE_URL
import com.example.btk_hackathon.data.local.database.BookDao
import com.example.btk_hackathon.data.local.database.BookDatabase
import com.example.btk_hackathon.data.local.model.remote.service.OpenLibraryBookApi
import com.example.btk_hackathon.data.repository.BookRepositoryImpl
import com.example.btk_hackathon.domain.repository.BookRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }


    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    fun provideApiService1(retrofit: Retrofit): OpenLibraryBookApi {
        return retrofit.create(OpenLibraryBookApi::class.java)
    }


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
        Sana bir kitap ismi verilecek. Bu isim Türkçe veya İngilizce olabilir. Bana bu kitabın aşağıdaki detaylarını JSON formatında, belirtilen anahtar isimleriyle döndür:
        
        - Uzun ve detaylı bir Türkçe özet ("özet")
        - Kitabın yazarı ("yazar")
        - Yazarın kısa biyografisi ("yazar_biografisi")
        - Kitabın yayın tarihi ("yayın_tarihi")
        - Kitabın türü ("tür")
        - Kitabın adı ("kitap_adı")
        
        JSON formatı şu şekilde olmalıdır:
        {
            "kitap_adı": "Kitabın adı burada",
            "yazar": "Yazar adı burada",
            "yazar_biografisi": "Yazar biyografisi burada",
            "tür": "Kitap türü burada",
            "yayın_tarihi": "Yayın tarihi burada",
            "özet": "Kitabın özeti burada"
        }
        """
                )
            })
    }


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookDatabase {
        return Room.databaseBuilder(
            context.applicationContext, BookDatabase::class.java, "books"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNumerologyDao(db: BookDatabase): BookDao {
        return db.bookDao()
    }

    @Provides
    fun provideBookRepository(
        openLibraryBookApi: OpenLibraryBookApi, generativeModel: GenerativeModel, bookDao: BookDao
    ): BookRepository {
        return BookRepositoryImpl(
            openLibraryBookApi = openLibraryBookApi,
            generativeModel = generativeModel,
            api = bookDao
        )
    }
}