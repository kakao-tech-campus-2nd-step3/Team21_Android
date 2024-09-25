package com.example.everymoment.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.everymoment.R
import com.example.everymoment.databinding.ActivityKakaoLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class KakaoLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKakaoLoginBinding
    private var userId: Long? = null
    private var userNickname: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e("kakaoLogin", "토큰 정보 보기 실패", error)
                // 로그인 화면 표시
                setupKakaoLogin()
            } else if (tokenInfo != null) {
                Log.i("kakaoLogin", "이미 로그인된 사용자: ${tokenInfo.id}")
                // 이미 로그인되어 있으면 바로 메인 화면으로 이동
                moveToMainScreen()
            }
        }
    }

    private fun setupKakaoLogin() {
        binding = ActivityKakaoLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.kakaoLoginButton.setOnClickListener {

            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("kakaoLogin", "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i("kakaoLogin", "카카오계정으로 로그인 성공 ${token.accessToken}")
                    saveUserInformation()
                    Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("kakaoLogin", "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i("kakaoLogin", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        saveUserInformation()
                        Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun saveUserInformation() {
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("kakaoLogin", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    "kakaoLogin", "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}"
                )
                val kakaoUserId = user.id
                val kakaoNickname = user.kakaoAccount?.profile?.nickname
            }
        }
        moveToMainScreen()
    }

    private fun moveToMainScreen() {
        val intent = Intent(this@KakaoLoginActivity, CalendarViewActivity::class.java)
        intent.putExtra("userId", userId)
        intent.putExtra("userNickname", userNickname)
        startActivity(intent)
        finish()
    }
}