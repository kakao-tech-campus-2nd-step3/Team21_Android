package com.example.everymoment.data.repository

import android.util.Log
import com.example.everymoment.data.model.network.api.PotatoCakeApiService
import com.example.everymoment.data.model.network.api.NetworkModule
import com.example.everymoment.data.model.network.dto.response.DiaryResponse
import com.example.everymoment.services.location.GlobalApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendDiaryRepository {
    private val apiService: PotatoCakeApiService = NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token = "Bearer $jwtToken"

    fun getFriendDiaries(
        friendId: Int,
        date: String,
        callback: (Boolean, DiaryResponse?) -> Unit
    ) {
        apiService.getFriendDiaries(token, friendId, date).enqueue(object : Callback<DiaryResponse> {
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

    fun getTotalFriendDiaries(
        date: String,
        callback: (Boolean, DiaryResponse?) -> Unit
    ) {
        apiService.getTotalFriendDiaries(token, date).enqueue(object : Callback<DiaryResponse> {
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
}