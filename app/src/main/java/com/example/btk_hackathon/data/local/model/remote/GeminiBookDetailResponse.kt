package com.example.btk_hackathon.data.local.model.remote

import com.example.btk_hackathon.data.local.model.local.Book
import com.google.gson.annotations.SerializedName

data class GeminiBookDetailResponse(
    @SerializedName("kitap_adı") val title: String,
    @SerializedName("yazar") val author: String,
    @SerializedName("yazar_biografisi") val authorBiography: String,
    @SerializedName("tür") val genre: String,
    @SerializedName("yayın_tarihi") val publicationDate: String,
    @SerializedName("özet") val summary: String
)


fun GeminiBookDetailResponse.toBook(cover_edition_key:String): Book {
    return Book(
    title = this.title,
    genre =this.genre,
    publicationDate = this.publicationDate,
    author =this.author,
    authorBiography =this.authorBiography,
    summary =this.summary,
    cover_edition_key = cover_edition_key,
    )
}