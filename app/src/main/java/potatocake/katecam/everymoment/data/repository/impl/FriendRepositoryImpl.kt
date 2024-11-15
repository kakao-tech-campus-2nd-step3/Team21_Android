package potatocake.katecam.everymoment.data.repository.impl

import android.util.Log
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.response.FriendRequestListResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.FriendsListResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.MemberResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.ServerResponse
import potatocake.katecam.everymoment.data.repository.FriendRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class FriendRepositoryImpl @Inject constructor(
    private val apiService: PotatoCakeApiService,
    @Named("jwtToken") private val token: String
) : FriendRepository {

    override fun getFriendsList(
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

    override fun deleteFriend(
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

    override fun getFriendRequestList(
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

    override fun acceptFriendRequest(
        requestId: Int,
        callback: (Boolean, String?) -> Unit
    ) {
        apiService.acceptFriendRequest(token, requestId).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful) {
                    Log.d("acceptFriendRequest", "${p1.body()}")
                    callback(true, p1.message())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("acceptFriendRequest", "Failed to accept friend request: ${p1.message}")
                callback(false, null)
            }
        })
    }

    override fun rejectFriendRequest(
        requestId: Int,
        callback: (Boolean, String?) -> Unit
    ) {
        apiService.rejectFriendRequest(token, requestId).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful) {
                    Log.d("rejectFriendRequest", "${p1.body()}")
                    callback(true, p1.message())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("rejectFriendRequest", "Failed to reject friend request: ${p1.message}")
                callback(false, null)
            }
        })
    }

    override fun getMembers(
        callback: (Boolean, MemberResponse?) -> Unit
    ) {
        apiService.getMembers(token)
            .enqueue(object : Callback<MemberResponse> {
                override fun onResponse(
                    p0: Call<MemberResponse>,
                    p1: Response<MemberResponse>
                ) {
                    if (p1.isSuccessful) {
                        Log.d("AllMembers", "${p1.body()}")
                        callback(true, p1.body())
                    } else {
                        callback(false, null)
                    }
                }

                override fun onFailure(p0: Call<MemberResponse>, p1: Throwable) {
                    Log.d("AllMembers", "Failed to fetch all members: ${p1.message}")
                    callback(false, null)
                }
            })
    }

    override fun sendFriendRequest(
        memberId: Int,
        callback: (Boolean, MemberResponse?) -> Unit
    ) {
        apiService.sendFriendRequest(token, memberId).enqueue(object : Callback<MemberResponse> {
            override fun onResponse(p0: Call<MemberResponse>, p1: Response<MemberResponse>) {
                if (p1.isSuccessful) {
                    Log.d("FriendRequestPost", "${p1.body()}")
                    callback(true, p1.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<MemberResponse>, p1: Throwable) {
                Log.d("FriendRequestPost", "Failed to fetch diaries: ${p1.message}")
                callback(false, null)
            }
        })
    }

    override fun getFriendsListWithPage(key: Int, callback: (Boolean, FriendsListResponse?) -> Unit) {
        apiService.getFriendsList(token, key).enqueue(object : Callback<FriendsListResponse> {
            override fun onResponse(
                call: Call<FriendsListResponse>,
                response: Response<FriendsListResponse>
            ) {
                if (response.isSuccessful) {
                    callback(true, response.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(call: Call<FriendsListResponse>, t: Throwable) {
                callback(false, null)
            }
        })
    }
}