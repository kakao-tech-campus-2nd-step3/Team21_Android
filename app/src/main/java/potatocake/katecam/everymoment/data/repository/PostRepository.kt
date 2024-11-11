package potatocake.katecam.everymoment.data.repository

import android.util.Log
import potatocake.katecam.everymoment.data.model.network.api.NetworkModule
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.request.PostCommentRequest
import potatocake.katecam.everymoment.data.model.network.dto.response.GetFilesResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.getFriendDiaryInDetail.GetFriendDiaryResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.ServerResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.getComments.GetCommentsResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.getLikeCnt.GetLikeCountResponse
import potatocake.katecam.everymoment.services.location.GlobalApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostRepository {
    private val apiService: PotatoCakeApiService =
        NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token = "Bearer $jwtToken"

    /**
     * 포스트
     */
    fun getDiaryinDetail(
        diaryId: Int,
        callback: (Boolean, GetFriendDiaryResponse?) -> Unit
    ) {
        apiService.getFriendDiaryInDetail(token, diaryId)
            .enqueue(object : Callback<GetFriendDiaryResponse> {
                override fun onResponse(
                    p0: Call<GetFriendDiaryResponse>,
                    p1: Response<GetFriendDiaryResponse>
                ) {
                    if (p1.isSuccessful) {
                        Log.d("getDiaryInDetail", "${p1.body()}")
                        callback(true, p1.body())
                    } else {
                        callback(false, null)
                    }
                }

                override fun onFailure(p0: Call<GetFriendDiaryResponse>, p1: Throwable) {
                    Log.d("settle54", "Failed to get FriendDiaryInDetail: ${p1.message}")
                    callback(false, null)
                }
            })
    }

    fun getFiles(diaryId: Int, callback: (Boolean, GetFilesResponse?) -> Unit) {
        apiService.getFiles(token, diaryId).enqueue(object : Callback<GetFilesResponse> {
            override fun onResponse(
                p0: Call<GetFilesResponse>,
                p1: Response<GetFilesResponse>
            ) {
                if (p1.isSuccessful) {
                    Log.d("settle54", "${p1.body()}")
                    callback(true, p1.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<GetFilesResponse>, p1: Throwable) {
                Log.d("settle54", "Failed to Get Files: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun postLike(diaryId: Int, callback: (Boolean, String?) -> Unit) {
        apiService.postLike(token, diaryId).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful) {
                    Log.d("settle54", "${p1.body()}")
                    callback(true, p1.message())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("settle54", "Failed to post like: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun getLikeCnt(diaryId: Int, callback: (Boolean, GetLikeCountResponse?) -> Unit) {
        apiService.getLikeCnt(token, diaryId).enqueue(object : Callback<GetLikeCountResponse> {
            override fun onResponse(
                p0: Call<GetLikeCountResponse>,
                p1: Response<GetLikeCountResponse>
            ) {
                if (p1.isSuccessful) {
                    Log.d("settle54", "${p1.body()}")
                    callback(true, p1.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<GetLikeCountResponse>, p1: Throwable) {
                Log.d("settle54", "Failed to Get Files: ${p1.message}")
                callback(false, null)
            }
        })
    }



    /**
     * 댓글
     */
    fun postComment(diaryId: Int, request: PostCommentRequest, callback: (Boolean, String?) -> Unit) {
        apiService.postComment(token, diaryId, request).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful) {
                    Log.d("settle54", "${p1.body()}")
                    callback(true, p1.message())
                } else {
                    callback(false, null)
                    Log.d("settle54", "fail")
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("settle54", "Failed to post category: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun delComment(commentId: Int, callback: (Boolean, String?) -> Unit) {
        apiService.delComment(token, commentId).enqueue(object : Callback<ServerResponse> {
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

    fun getComments(diaryId:Int, key: Int, callback: (Boolean, GetCommentsResponse?) -> Unit) {
        apiService.getComments(token, diaryId, key).enqueue(object : Callback<GetCommentsResponse> {
            override fun onResponse(
                p0: Call<GetCommentsResponse>,
                p1: Response<GetCommentsResponse>
            ) {
                if (p1.isSuccessful) {
                    Log.d("settle54", "${p1.body()}")
                    callback(true, p1.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<GetCommentsResponse>, p1: Throwable) {
                Log.d("settle54", "Failed to Get Files: ${p1.message}")
                callback(false, null)
            }
        })
    }


}