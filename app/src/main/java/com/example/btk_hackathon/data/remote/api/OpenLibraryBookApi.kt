package com.example.btk_hackathon.data.remote.api

import com.example.btk_hackathon.data.remote.dto.OpenLibraryBookModel
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryBookApi {
    @GET("search.json")
    suspend fun getData(
        @Query("title") title: String?
    ): OpenLibraryBookModel
}