package com.example.btk_hackathon.domain.model

import com.example.btk_hackathon.data.remote.dto.Doc

data class BookDto(
    val coverEditionKey: String?,
    val authorName: List<String>,
    val title: String,
    val person: List<String>
)


fun Doc.toBookDto(): BookDto {
    return BookDto(
        coverEditionKey = "https://covers.openlibrary.org/b/olid/${this.cover_edition_key}-L.jpg",
        authorName = this.author_name ?: listOf("Unknown Author"),
        title = this.title ?: "Unknown Title",
        person = this.person ?: emptyList()
    )
}