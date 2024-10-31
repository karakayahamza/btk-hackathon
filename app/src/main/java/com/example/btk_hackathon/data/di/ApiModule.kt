package com.example.btk_hackathon.data.di

import com.example.btk_hackathon.Util.Util.BASE_URL
import com.example.btk_hackathon.data.remote.api.OpenLibraryBookApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenLibraryBookApi(retrofit: Retrofit): OpenLibraryBookApi {
        return retrofit.create(OpenLibraryBookApi::class.java)
    }
}
