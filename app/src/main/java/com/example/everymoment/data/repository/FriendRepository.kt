package com.example.everymoment.data.repository

import android.util.Log
import com.example.everymoment.GlobalApplication
import com.example.everymoment.data.model.NetworkModule
import com.example.everymoment.data.model.PotatoCakeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendRepository {
    private val apiService: PotatoCakeApiService =
        NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token = "Bearer $jwtToken"

    fun getFriendsList(
        callback: (Boolean, FriendsListResponse?) -> Unit
    ) {
        apiService.getFriendsList(token).enqueue(object : Callback<FriendsListResponse> {
            override fun onResponse(
                p0: Call<FriendsListResponse>,
                p1: Response<FriendsListResponse>
            ) {
                if (p1.isSuccessful) {
                    Log.d("FriendsList", "${p1.body()}")
                    callback(true, p1.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<FriendsListResponse>, p1: Throwable) {
                Log.d("FriendsList", "Failed to fetch friends list: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun deleteFriend(
        friendId: Int,
        callback: (Boolean, String?) -> Unit
    ) {
        apiService.deleteFriend(token, friendId).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful) {
                    Log.d("DeleteFriend", "${p1.body()}")
                    callback(true, p1.message())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("DeleteFriend", "Failed to delete friend: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun getFriendRequestList(
        callback: (Boolean, FriendRequestListResponse?) -> Unit
    ) {
        apiService.getFriendRequestList(token)
            .enqueue(object : Callback<FriendRequestListResponse> {
                override fun onResponse(
                    p0: Call<FriendRequestListResponse>,
                    p1: Response<FriendRequestListResponse>
                ) {
                    if (p1.isSuccessful) {
                        Log.d("FriendRequestList", "${p1.body()}")
                        callback(true, p1.body())
                    } else {
                        callback(false, null)
                    }
                }

                override fun onFailure(p0: Call<FriendRequestListResponse>, p1: Throwable) {
                    Log.d("FriendRequestList", "Failed to fetch friend request list: ${p1.message}")
                    callback(false, null)
                }
            })
    }
}