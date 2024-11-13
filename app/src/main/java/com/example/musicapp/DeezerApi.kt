package com.example.musicapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DeezerApi {
    @GET("search")
    fun searchTracks(
        @Header("x-rapidapi-host") host: String = "deezerdevs-deezer.p.rapidapi.com", // Host của API
        @Header("x-rapidapi-key") apiKey: String, // API Key
        @Query("q") query: String // Từ khóa tìm kiếm
    ): Call<DeezerTrackResponse>
}

