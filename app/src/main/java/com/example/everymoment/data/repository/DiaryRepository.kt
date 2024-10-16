package com.example.everymoment.data.repository

import android.util.Log
import com.example.everymoment.GlobalApplication
import com.example.everymoment.data.dto.CategoryRequest
import com.example.everymoment.data.dto.DetailDiaryResponse
import com.example.everymoment.data.dto.GetCategoriesResponse
import com.example.everymoment.data.model.NetworkModule
import com.example.everymoment.data.model.PotatoCakeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryRepository {
    private val apiService: PotatoCakeApiService =
        NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token =
        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6OCwiaWF0IjoxNzI4OTcxOTEzLCJleHAiOjE3MjkxNDQ3MTN9.2zDfm4HdW8D4QcZm4hxXx4WJTOyPxnZ-sqvMraKTOT8"

    fun getDiaries(
        date: String,
        callback: (Boolean, DiaryResponse?) -> Unit
    ) {
        apiService.getDiaries(token, date).enqueue(object : Callback<DiaryResponse> {
            override fun onResponse(p0: Call<DiaryResponse>, p1: Response<DiaryResponse>) {
                if (p1.isSuccessful) {
                    Log.d("arieum", "${p1.body()}")
                    callback(true, p1.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<DiaryResponse>, p1: Throwable) {
                Log.d("arieum", "Failed to fetch diaries: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun updateBookmarkStatus(
        diaryId: Int,
        callback: (Boolean, String?) -> Unit
    ) {
        apiService.updateBookmarkStatus(token, diaryId).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful) {
                    Log.d("arieum", "${p1.body()}")
                    callback(true, p1.message())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("arieum", "Failed to update bookmark state: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun updateShareStatus(
        diaryId: Int,
        callback: (Boolean, String?) -> Unit
    ) {
        apiService.updateShareStatus(token, diaryId).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful) {
                    Log.d("arieum", "${p1.body()}")
                    callback(true, p1.message())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("arieum", "Failed to update share state: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun deleteDiary(
        diaryId: Int,
        callback: (Boolean, String?) -> Unit
    ) {
        apiService.deleteDiary(token, diaryId).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful) {
                    Log.d("arieum", "${p1.body()}")
                    callback(true, p1.message())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("arieum", "Failed to delete diary: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun getDiaryinDetail(
        diaryId: Int,
        callback: (Boolean, DetailDiaryResponse?) -> Unit
    ) {
        apiService.getDiaryinDetail(token, diaryId).enqueue(object : Callback<DetailDiaryResponse> {
            override fun onResponse(
                p0: Call<DetailDiaryResponse>,
                p1: Response<DetailDiaryResponse>
            ) {
                if (p1.isSuccessful) {
                    Log.d("settle54", "${p1.body()}")
                    callback(true, p1.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<DetailDiaryResponse>, p1: Throwable) {
                Log.d("settle54", "Failed to get diaryInDetail: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun postCategory(
        categoryName: String, callback: (Boolean, String?) -> Unit
    ) {
        val categoryRequest = CategoryRequest(categoryName)
        apiService.postCategory(token, categoryRequest).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful) {
                    Log.d("settle54", "${p1.body()}")
                    callback(true, p1.message())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("settle54", "Failed to post category: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun delCategory(categoryId: Int,callback: (Boolean, String?) -> Unit) {
        apiService.delCategory(token, categoryId).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(
                p0: Call<ServerResponse>,
                p1: Response<ServerResponse>
            ) {
                if (p1.isSuccessful) {
                    Log.d("settle54", "${p1.body()}")
                    callback(true, p1.message())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("settle54", "Failed to get diaryInDetail: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun getCategories(callback: (Boolean, GetCategoriesResponse?) -> Unit) {
        apiService.getCategories(token).enqueue(object : Callback<GetCategoriesResponse> {
            override fun onResponse(
                p0: Call<GetCategoriesResponse>,
                p1: Response<GetCategoriesResponse>
            ) {
                if (p1.isSuccessful) {
                    callback(true, p1.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<GetCategoriesResponse>, p1: Throwable) {
                callback(false, null)
            }
        })
    }


}