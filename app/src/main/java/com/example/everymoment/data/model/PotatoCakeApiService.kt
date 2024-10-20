package com.example.everymoment.data.model

import com.example.everymoment.data.model.network.dto.response.GetDetailDiaryResponse
import com.example.everymoment.data.model.network.dto.response.GetCategoriesResponse
import com.example.everymoment.data.model.network.dto.response.GetFilesResponse
import com.example.everymoment.data.model.network.dto.request.PostCategoryRequest
import com.example.everymoment.data.model.network.dto.request.PostFilesRequest
import com.example.everymoment.data.repository.DiaryResponse
import com.example.everymoment.data.repository.ServerResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
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

    @GET("/api/diaries/my/{diaryId}")
    fun getDiaryInDetail(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<GetDetailDiaryResponse>

    @POST("/api/categories")
    fun postCategory(
        @Header("Authorization") token: String,
        @Body categoryRequest: PostCategoryRequest
    ): Call<ServerResponse>

    @DELETE("/api/categories/{categoryId}")
    fun delCategory(
        @Header("Authorization") token: String,
        @Path("categoryId") categoryId: Int
    ): Call<ServerResponse>

    @GET("/api/categories")
    fun getCategories(
        @Header("Authorization") token: String
    ): Call<GetCategoriesResponse>

    @GET("/api/diaries/{diaryId}/files")
    fun getFiles(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<GetFilesResponse>

    @POST("/api/diaries/{diaryId}/files")
    fun postFiles(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int,
        @Body files: PostFilesRequest
    ): Call<ServerResponse>

}