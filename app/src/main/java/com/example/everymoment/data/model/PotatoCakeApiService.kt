package com.example.everymoment.data.model

import com.example.everymoment.data.repository.DiaryResponse
import com.example.everymoment.data.repository.ServerResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface PotatoCakeApiService {
    @GET("api/diaries/my")
    fun getDiaries(
        @Header("Authorization") token: String,
        @Query("date") date: String
    ): Call<DiaryResponse>

    @PATCH("api/diaries/{diaryId}/bookmark")
    fun updateBookmarkStatus(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<ServerResponse>

    @PATCH("api/diaries/{diaryId}/privacy")
    fun updateShareStatus(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<ServerResponse>

    @DELETE("api/diaries/{diaryId}")
    fun deleteDiary(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<ServerResponse>
}