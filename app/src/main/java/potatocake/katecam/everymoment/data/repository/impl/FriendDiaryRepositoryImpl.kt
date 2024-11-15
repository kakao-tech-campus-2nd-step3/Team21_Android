package potatocake.katecam.everymoment.data.repository.impl

import android.util.Log
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.response.DiaryResponse
import potatocake.katecam.everymoment.data.repository.FriendDiaryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class FriendDiaryRepositoryImpl @Inject constructor(
    private val apiService: PotatoCakeApiService,
    @Named("jwtToken") private val token: String
) : FriendDiaryRepository {

    override fun getFriendDiaries(
        friendId: Int,
        callback: (Boolean, DiaryResponse?) -> Unit
    ) {
        apiService.getFriendDiaries(token, friendId).enqueue(object : Callback<DiaryResponse> {
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

    override fun getFriendDiariesWithPage(
        friendId: Int,
        page: Int,
        callback: (Boolean, DiaryResponse?) -> Unit
    ) {
        apiService.getFriendDiaries(token, friendId, page)
            .enqueue(object : Callback<DiaryResponse> {
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

    override fun getTotalFriendDiaries(
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