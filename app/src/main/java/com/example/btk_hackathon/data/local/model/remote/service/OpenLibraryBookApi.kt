package com.example.btk_hackathon.data.local.model.remote.service

import com.example.btk_hackathon.data.local.model.remote.OpenLibraryBookModel
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryBookApi {
    @GET("search.json")
    suspend fun getData(
        @Query("title") title: String?
    ): OpenLibraryBookModel
}