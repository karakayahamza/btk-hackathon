package com.example.btk_hackathon.data.di

import com.example.btk_hackathon.data.local.dao.BookDao
import com.example.btk_hackathon.data.local.repository.LocalBookRepositoryImpl
import com.example.btk_hackathon.data.remote.api.OpenLibraryBookApi
import com.example.btk_hackathon.data.remote.repository.RemoteBookRepositoryImpl
import com.example.btk_hackathon.domain.repository.LocalBookRepository
import com.example.btk_hackathon.domain.repository.RemoteBookRepository
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRemoteBookRepository(
        openLibraryBookApi: OpenLibraryBookApi,
        generativeModel: GenerativeModel,
    ): RemoteBookRepository {
        return RemoteBookRepositoryImpl(
            openLibraryBookApi = openLibraryBookApi,
            generativeModel = generativeModel,
        )
    }

    @Provides
    @Singleton
    fun provideLocalBookRepository(
        bookDao: BookDao
    ): LocalBookRepository {
        return LocalBookRepositoryImpl(
            bookDao = bookDao
        )
    }
}
