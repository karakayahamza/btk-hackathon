package com.example.btk_hackathon.domain.model

import com.example.btk_hackathon.data.local.model.remote.Doc

data class BookDto(
    val coverEditionKey: String?,
    val authorName: List<String>,
    val ratingsCount: Int,
    val title: String
)

fun Doc.toBookDto(): BookDto {
    return BookDto(
        coverEditionKey = "https://covers.openlibrary.org/b/olid/${this.cover_edition_key}-L.jpg",
        authorName = this.author_name ?: listOf("Unknown Author"),
        ratingsCount = this.ratings_count ?: 0,
        title = this.title ?: "Unknown Title"
    )
}