package potatocake.katecam.everymoment.data.repository

import android.app.Activity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.model.AccessTokenInfo
import com.kakao.sdk.user.model.User
import potatocake.katecam.everymoment.data.model.network.dto.response.NonLoginUserNumberResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.ServerResponse

interface UserRepository {
    fun getKakaoTokenInfo(callback: (AccessTokenInfo?, Throwable?) -> Unit)

    fun loginWithKakaoTalk(
        activity: Activity,
        callback: (OAuthToken?, Throwable?) -> Unit
    )

    fun loginWithKakaoAccount(
        activity: Activity,
        callback: (OAuthToken?, Throwable?) -> Unit
    )

    fun requestUserInfo(callback: (User?, Throwable?) -> Unit)

    fun requestToken(userId: Long?, nickname: String?)

    fun getAnonymousLogin(
        callback: (Boolean, NonLoginUserNumberResponse?) -> Unit
    )

    fun postToken(
        fcmToken: String,
        deviceId: String,
        callback: (Boolean, ServerResponse?) -> Unit
    )

}