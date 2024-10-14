package com.example.everymoment.data.repository

import android.util.Log
import com.example.everymoment.GlobalApplication
import com.example.everymoment.data.model.NetworkModule
import com.example.everymoment.data.model.PotatoCakeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryRepository {
    private val apiService: PotatoCakeApiService = NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token = "Bearer $jwtToken"

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
}