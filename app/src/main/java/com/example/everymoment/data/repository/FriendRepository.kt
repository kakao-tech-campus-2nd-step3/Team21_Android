package com.example.everymoment.data.repository

import android.util.Log
import com.example.everymoment.GlobalApplication
import com.example.everymoment.data.model.NetworkModule
import com.example.everymoment.data.model.PotatoCakeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendRepository {
    private val apiService: PotatoCakeApiService =
        NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token = "Bearer $jwtToken"

    suspend fun getFriendsList(
        onSuccess: (FriendsListResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getFriendsList(token)
                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onError("Failed to fetch friends list: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FriendRepository", "Error fetching friends list", e)
                onError("Failed to fetch friends list: ${e.message}")
            }
        }
    }

    fun deleteFriend(
        friendId: Int,
        callback: (Boolean, String?) -> Unit
    ) {
        apiService.deleteFriend(token, friendId).enqueue(object : Callback<ServerResponse> {
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