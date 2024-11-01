package com.example.btk_hackathon.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "genre")
    val genre: String,
    @ColumnInfo(name = "publication_date")
    val publicationDate: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "author_biography")
    val authorBiography: String,
    @ColumnInfo(name = "summary")
    val summary: String,
    @ColumnInfo(name = "cover_edition_key")
    val cover_edition_key: String,
    @ColumnInfo(name = "main_characters")
    val main_characters: List<String>
)