package com.example.btk_hackathon.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeminiBookModel(
    @SerializedName("book_name") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("author_biography") val authorBiography: String,
    @SerializedName("genre") val genre: String,
    @SerializedName("publication_date") val publicationDate: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("main_characters") val main_characters: List<String>,
)