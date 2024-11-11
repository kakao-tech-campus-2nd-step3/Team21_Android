package potatocake.katecam.everymoment.data.repository

import android.app.Activity
import android.util.Log
import potatocake.katecam.everymoment.data.model.network.api.NetworkModule
import potatocake.katecam.everymoment.services.location.GlobalApplication
import potatocake.katecam.everymoment.data.model.network.api.NetworkUtil
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.response.NonLoginUserNumberResponse
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.AccessTokenInfo
import com.kakao.sdk.user.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val apiService: PotatoCakeApiService =
        NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token =
        "Bearer $jwtToken"

    fun getKakaoTokenInfo(callback: (AccessTokenInfo?, Throwable?) -> Unit) {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            callback(tokenInfo, error)
        }
    }

    fun loginWithKakaoTalk(
        activity: Activity,
        callback: (OAuthToken?, Throwable?) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
            callback(token, error)
        }
    }

    fun loginWithKakaoAccount(
        activity: Activity,
        callback: (OAuthToken?, Throwable?) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
    }

    fun requestUserInfo(callback: (User?, Throwable?) -> Unit) {
        UserApiClient.instance.me { user, error ->
            callback(user, error)
        }
    }

    fun requestToken(userId: Long?, nickname: String?) {
        NetworkUtil.sendData(
            "http://13.125.156.74:8080/api/members/login",
            null,
            mapOf(
                "number" to userId,
                "nickname" to nickname
            )
        ) { success, code, message, infoObject->
            if (success) {
                val token = infoObject?.get("token")?.asString

                if (token != null) {
                    GlobalApplication.prefs.setString("token", token)
                }

                Log.d("arieum", "서버 응답: $token")
            } else {
                Log.d("arieum", "Network failed")
            }
        }
    }

    fun getAnonymousLogin(
        callback: (Boolean, NonLoginUserNumberResponse?) -> Unit
    ) {
        apiService.getAnonymousLogin().enqueue(object : Callback<NonLoginUserNumberResponse> {
            override fun onResponse(
                call: Call<NonLoginUserNumberResponse>,
                response: Response<NonLoginUserNumberResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        // 회원번호가 null이면 새로운 익명 계정 생성
                        if (it.info.number == null) {
                            callback(true, it)
                        } else {
                            // 회원번호가 있으면 해당 번호로 로그인
                            callback(true, it)
                        }
                    } ?: callback(false, null)
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(call: Call<NonLoginUserNumberResponse>, t: Throwable) {
                Log.d("AnonymousLogin", "Failed to AnonymousLogin: ${t.message}")
                callback(false, null)
            }
        })
    }
}