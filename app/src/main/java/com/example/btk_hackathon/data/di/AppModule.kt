package com.example.btk_hackathon.data.di

import android.content.Context
import androidx.room.Room
import com.example.btk_hackathon.data.local.database.BookDatabase
import com.example.btk_hackathon.data.repository.BookRepositoryImpl
import com.example.btk_hackathon.domain.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BookDatabase::class.java,
            "books"
        ).build()
    }

    @Provides
    fun provideNoteDao(database: BookDatabase): BookRepository {
        return BookRepositoryImpl(api = database.noteDao())
    }
}