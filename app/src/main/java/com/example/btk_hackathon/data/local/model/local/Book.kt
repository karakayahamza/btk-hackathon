package com.example.btk_hackathon.data.local.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
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
    val cover_edition_key: String
)

//fun fetchAndSaveBook() {
//    // API'den veri alma işlemi
//    val apiResponse: GeminiBookDetailResponse = apiService.getBookDetail() // API çağrısı
//
//    // Dönüşüm yapma
//    val bookToSave = apiResponse.toBook()
//
//    // Veritabanına kaydetme
//    CoroutineScope(Dispatchers.IO).launch {
//        bookDao.insert(bookToSave) // Room DAO'su ile kaydetme
//    }
//}