package potatocake.katecam.everymoment.data.repository.impl

import android.app.Activity
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.AccessTokenInfo
import com.kakao.sdk.user.model.User
import potatocake.katecam.everymoment.GlobalApplication
import potatocake.katecam.everymoment.data.model.network.api.NetworkUtil
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.request.TokenRequest
import potatocake.katecam.everymoment.data.model.network.dto.response.NonLoginUserNumberResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.ServerResponse
import potatocake.katecam.everymoment.data.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    private val apiService: PotatoCakeApiService,
    @Named("jwtToken") private val token: String
) : UserRepository {

    override fun getKakaoTokenInfo(callback: (AccessTokenInfo?, Throwable?) -> Unit) {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            callback(tokenInfo, error)
        }
    }

    override fun loginWithKakaoTalk(
        activity: Activity,
        callback: (OAuthToken?, Throwable?) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
            callback(token, error)
        }
    }

    override fun loginWithKakaoAccount(
        activity: Activity,
        callback: (OAuthToken?, Throwable?) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
    }

    override fun requestUserInfo(callback: (User?, Throwable?) -> Unit) {
        UserApiClient.instance.me { user, error ->
            callback(user, error)
        }
    }

    override fun requestToken(userId: Long?, nickname: String?) {
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

    private fun saveAnonymousNumber(number: Int?) {
        GlobalApplication.prefs.setInt(KEY_ANONYMOUS_NUMBER, number)
    }

    private fun getStoredAnonymousNumber(): Int? {
        return GlobalApplication.prefs.getInt(KEY_ANONYMOUS_NUMBER)
    }

    override fun getAnonymousLogin(
        callback: (Boolean, NonLoginUserNumberResponse?) -> Unit
    ) {
        val storedNumber = getStoredAnonymousNumber()
        val storedToken = GlobalApplication.prefs.getString("token", null)

        apiService.getAnonymousLogin(storedNumber)
            .enqueue(object : Callback<NonLoginUserNumberResponse> {
                override fun onResponse(
                    call: Call<NonLoginUserNumberResponse>,
                    response: Response<NonLoginUserNumberResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            responseBody.info.number?.let { number ->
                                saveAnonymousNumber(number)
                            }

                            responseBody.info.token?.let { token ->
                                GlobalApplication.prefs.setString("token", token)
                                Log.d("AnonymousLogin", "Token saved: $token")
                            }

                            callback(true, responseBody)
                        } ?: callback(false, null)
                    } else {
                        Log.d("AnonymousLogin", "Response not successful: ${response.code()}")
                        callback(false, null)
                    }
                }

                override fun onFailure(call: Call<NonLoginUserNumberResponse>, t: Throwable) {
                    Log.d("AnonymousLogin", "Failed to AnonymousLogin: ${t.message}")
                    callback(false, null)
                }
            })
    }

    override fun postToken(
        fcmToken: String,
        deviceId: String,
        callback: (Boolean, ServerResponse?) -> Unit
    ) {
        val tokenRequest = TokenRequest(fcmToken, deviceId)

        apiService.postToken(token, tokenRequest).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(
                p0: Call<ServerResponse>,
                p1: Response<ServerResponse>
            ) {
                if (p1.isSuccessful) {
                    Log.d("FCMToken", "${p1.body()}")
                    callback(true, p1.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("FCMToken", "Failed to post FCM Token: ${p1.message}")
                callback(false, null)
            }
        })
    }
    companion object {
        private const val KEY_ANONYMOUS_NUMBER = "anonymous_user_number"
    }
}