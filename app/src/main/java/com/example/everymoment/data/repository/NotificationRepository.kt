package com.example.everymoment.data.repository

import android.util.Log
import com.example.everymoment.GlobalApplication
import com.example.everymoment.data.model.NetworkModule
import com.example.everymoment.data.model.PotatoCakeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationRepository {
    private val apiService: PotatoCakeApiService = NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token = "Bearer $jwtToken"
    fun getNotificationList(
        callback: (Boolean, NotificationResponse?) -> Unit
    ){
        apiService.getNotifications(token).enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(p0: Call<NotificationResponse>, p1: Response<NotificationResponse>) {
                if (p1.isSuccessful) {
                    Log.d("arieum", "${p1.body()}")
                    callback(true, p1.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<NotificationResponse>, p1: Throwable) {
                Log.d("arieum", "Failed to fetch notification list: ${p1.message}")
                callback(false, null)
            }
        })
    }
}