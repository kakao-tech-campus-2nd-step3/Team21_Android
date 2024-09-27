package com.example.everymoment.data.repository

import android.app.Activity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.AccessTokenInfo
import com.kakao.sdk.user.model.User

class UserRepository {

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
}