package com.example.btk_hackathon.data.remote.dto

data class OpenLibraryBookModel(
    val docs: List<Doc>,
    val numFound: Int,
    val numFoundExact: Boolean,
    val num_found: Int,
    val offset: Any,
    val q: String,
    val start: Int
)