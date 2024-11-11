package potatocake.katecam.everymoment.data.repository

import android.util.Log
import potatocake.katecam.everymoment.data.model.network.api.NetworkModule
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.response.MyInformationResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.ServerResponse
import potatocake.katecam.everymoment.services.location.GlobalApplication
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoRepository {
    private val apiService: PotatoCakeApiService =
        NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token =
        "Bearer $jwtToken"

    fun getMyInfo(
        callback: (Boolean, MyInformationResponse?) -> Unit
    ) {
        apiService.getMyInfo(token).enqueue(object : Callback<MyInformationResponse> {
            override fun onResponse(
                p0: Call<MyInformationResponse>,
                p1: Response<MyInformationResponse>
            ) {
                if (p1.isSuccessful) {
                    callback(true, p1.body())
                    Log.d("arieum", p1.body().toString())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<MyInformationResponse>, p1: Throwable) {
                Log.d("arieum", "Failed to fetch myinfo: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun updateMyInfo(
        nickname: RequestBody? = null,
        profileImg: MultipartBody.Part? = null,
        callback: (Boolean, ServerResponse) -> Unit
    ) {
        apiService.updateProfile(token, nickname, profileImg).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful) {
                    p1.body()?.let { callback(true, it) }
                    Log.d("arieum", p1.body().toString())
                } else {
                    Log.d("settle54", "fail")
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("arieum", p1.message.toString())
            }
        })
    }
}