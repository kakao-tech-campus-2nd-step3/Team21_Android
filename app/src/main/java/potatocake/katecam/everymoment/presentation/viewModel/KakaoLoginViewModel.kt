package potatocake.katecam.everymoment.presentation.viewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import potatocake.katecam.everymoment.data.model.network.dto.vo.KakaoLoginUiState
import potatocake.katecam.everymoment.data.repository.UserRepository
import potatocake.katecam.everymoment.di.UserRepositoryQualifier
import javax.inject.Inject

@HiltViewModel
class KakaoLoginViewModel @Inject constructor(
    @UserRepositoryQualifier
    private val userRepository: UserRepository
) : ViewModel() {

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
                userRepository.requestToken(user.id, user.kakaoAccount?.profile?.nickname)
                _uiState.value = KakaoLoginUiState(
                    isLoggedIn = true,
                    userId = user.id,
                    userNickname = user.kakaoAccount?.profile?.nickname
                )
            }
        }
    }

    fun getAnonymousLogin(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            userRepository.getAnonymousLogin() { success, response ->
                if (success) {
                    response?.info?.number?.let { number ->
                    }
                }
                onComplete(success)
            }
        }
    }
}