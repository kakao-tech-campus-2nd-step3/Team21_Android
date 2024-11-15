package potatocake.katecam.everymoment.data.repository.impl

import android.util.Log
import okhttp3.MultipartBody
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.response.MyInformationResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.ServerResponse
import potatocake.katecam.everymoment.data.repository.MyInfoRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class MyInfoRepositoryImpl @Inject constructor(
    private val apiService: PotatoCakeApiService,
    @Named("jwtToken") private val token: String
) : MyInfoRepository {

    override fun getMyInfo(
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

    override fun updateMyInfo(
        nickname: String?,
        profileImg: MultipartBody.Part?,
        callback: (Boolean, ServerResponse) -> Unit
    ) {
        val part = if (profileImg == null) {
            MultipartBody.Part.createFormData("emptyPart", "")
        } else {
            profileImg
        }
        Log.d("myInfo", "profileImg: $part")
        apiService.updateProfile(token, nickname, part).enqueue(object : Callback<ServerResponse> {
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