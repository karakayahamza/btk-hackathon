package com.example.btk_hackathon.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.btk_hackathon.data.local.dao.BookDao
import com.example.btk_hackathon.data.local.model.BookEntity

@Database(entities = [BookEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}