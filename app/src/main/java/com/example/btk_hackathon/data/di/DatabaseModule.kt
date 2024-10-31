package com.example.btk_hackathon.data.di

import android.content.Context
import androidx.room.Room
import com.example.btk_hackathon.data.local.dao.BookDao
import com.example.btk_hackathon.data.local.database.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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
    @Singleton
    fun provideBookDao(db: BookDatabase): BookDao {
        return db.bookDao()
    }
}
