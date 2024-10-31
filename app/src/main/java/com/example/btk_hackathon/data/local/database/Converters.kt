package com.example.btk_hackathon.data.local.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromCharacterList(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toCharacterList(value: String?): List<String> {
        return value?.split(",")?.map { it.trim() } ?: emptyList() // Virgülle ayrılmış stringi listeye dönüştür
    }
}