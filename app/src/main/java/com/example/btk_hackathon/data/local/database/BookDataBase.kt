package com.example.btk_hackathon.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.btk_hackathon.data.local.model.Book

@Database(entities = [Book::class], version = 1)
@TypeConverters(Converters::class)
abstract class BookDatabase : RoomDatabase() {
    abstract fun noteDao(): BookDao
}