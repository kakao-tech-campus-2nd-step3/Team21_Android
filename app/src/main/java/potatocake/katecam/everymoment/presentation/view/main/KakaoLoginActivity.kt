package potatocake.katecam.everymoment.presentation.view.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import potatocake.katecam.everymoment.databinding.ActivityKakaoLoginBinding
import potatocake.katecam.everymoment.presentation.view.sub.OnBoardingActivity
import potatocake.katecam.everymoment.presentation.viewModel.KakaoLoginViewModel

@AndroidEntryPoint
class KakaoLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKakaoLoginBinding
    private val viewModel: KakaoLoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKakaoLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupNonLogin()

        viewModel.uiState.observe(this) { uiState ->
            if (uiState.isLoggedIn) {
                moveToMainScreen(uiState.userId, uiState.userNickname)
            } else {
                uiState.errorMessage?.let { showError(it) }
                setupKakaoLogin()
            }
        }
    }

    private fun setupKakaoLogin() {
        binding.kakaoLoginButton.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                viewModel.loginWithKakaoTalk(this)
            } else {
                viewModel.loginWithKakaoAccount(this)
            }
        }
    }

    private fun setupNonLogin() {
        binding.nonLoginButton.setOnClickListener {
            viewModel.getAnonymousLogin { success ->
                if (success) {
                    val isOnboardingCompleted = getSharedPreferences("onboarding", MODE_PRIVATE)
                        .getBoolean("completed", false)

                    val intent = if (isOnboardingCompleted) {
                        Intent(this, MainActivity::class.java)
                    } else {
                        Intent(this, OnBoardingActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun moveToMainScreen(userId: Long?, userNickname: String?) {
        val isOnboardingCompleted = getSharedPreferences("onboarding", MODE_PRIVATE)
            .getBoolean("completed", false)

        val intent = if (isOnboardingCompleted) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, OnBoardingActivity::class.java)
        }

        intent.putExtra("userId", userId)
        intent.putExtra("userNickname", userNickname)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}