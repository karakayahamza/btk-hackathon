package com.example.btk_hackathon.data.remote.api

import com.example.btk_hackathon.data.remote.dto.OpenLibraryBookModel
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryBookApi {
    @GET("search.json")
    suspend fun getData(
        @Query("q") title: String?,
        @Query("lang") language: String = "tur"
    ): OpenLibraryBookModel
}

//https://openlibrary.org/search.json?q=harry%20potter%20ve%20s%C4%B1rlar%20odas%C4%B1&availability&lang=tr&limit=1
