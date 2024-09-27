package com.example.everymoment.presentation.viewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.everymoment.data.repository.UserRepository
import com.example.everymoment.presentation.KakaoLoginUiState

class KakaoLoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableLiveData<KakaoLoginUiState>()
    val uiState: LiveData<KakaoLoginUiState> = _uiState

    init {
        checkKakaoLoginStatus()
    }

    fun checkKakaoLoginStatus() {
        userRepository.getKakaoTokenInfo { tokenInfo, error ->
            if (error != null || tokenInfo == null) {
                _uiState.value = KakaoLoginUiState(errorMessage = "로그인 필요")
            } else {
                _uiState.value = KakaoLoginUiState(isLoggedIn = true)
                saveUserInfo()
            }
        }
    }

    fun loginWithKakaoTalk(activity: Activity) {
        userRepository.loginWithKakaoTalk(activity) { token, error ->
            if (error != null || token == null) {
                _uiState.value = KakaoLoginUiState(errorMessage = "카카오톡 로그인 실패")
            } else {
                saveUserInfo()
            }
        }
    }

    fun loginWithKakaoAccount(activity: Activity) {
        userRepository.loginWithKakaoAccount(activity) { token, error ->
            if (error != null || token == null) {
                _uiState.value = KakaoLoginUiState(errorMessage = "카카오계정 로그인 실패")
            } else {
                saveUserInfo()
            }
        }
    }

    private fun saveUserInfo() {
        userRepository.requestUserInfo { user, error ->
            if (user != null) {
                Log.i(
                    "kakaoLogin", "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}"
                )
                _uiState.value = KakaoLoginUiState(
                    isLoggedIn = true,
                    userId = user.id,
                    userNickname = user.kakaoAccount?.profile?.nickname
                )
            }
        }
    }
}