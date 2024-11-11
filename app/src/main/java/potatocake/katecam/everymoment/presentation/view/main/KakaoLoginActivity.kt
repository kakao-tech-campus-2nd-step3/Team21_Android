package potatocake.katecam.everymoment.presentation.view.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.UserRepository
import potatocake.katecam.everymoment.databinding.ActivityKakaoLoginBinding
import potatocake.katecam.everymoment.presentation.view.sub.OnBoardingActivity
import potatocake.katecam.everymoment.presentation.viewModel.KakaoLoginViewModel
import potatocake.katecam.everymoment.presentation.viewModel.factory.KakaoLoginViewModelFactory
import com.kakao.sdk.user.UserApiClient

class KakaoLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKakaoLoginBinding
    private lateinit var viewModel: KakaoLoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKakaoLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupNonLogin()

        val userRepository = UserRepository()
        viewModel = ViewModelProvider(this, KakaoLoginViewModelFactory(userRepository)).get(
            KakaoLoginViewModel::class.java
        )

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
            viewModel.getAnonymousLogin()
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
            finish()
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