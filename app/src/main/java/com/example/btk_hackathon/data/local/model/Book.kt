package com.example.btk_hackathon.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val kitap_adi: String,
    val yazar: String,
    val yayin_yili: Int,
    val ortalama_puan: Float,
    val ozet: String,
    val ana_karakterler: List<String>
)