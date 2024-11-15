package potatocake.katecam.everymoment.presentation.view.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import potatocake.katecam.everymoment.GlobalApplication
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.repository.UserRepository
import potatocake.katecam.everymoment.databinding.ActivityMainBinding
import potatocake.katecam.everymoment.di.UserRepositoryQualifier
import potatocake.katecam.everymoment.presentation.view.main.search.SearchFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    @UserRepositoryQualifier
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getFCMToken()

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        fetchValues()

        if (savedInstanceState == null) {
            val fragment = TodayLogFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        initNavigationBar()
    }

    fun hideNavigationBar() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showNavigationBar() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun fetchValues() {
        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val serviceState = Firebase.remoteConfig.getString("serviceState")
                val serviceMessage = Firebase.remoteConfig.getString("serviceMessage")
                Log.d("testt", "state: $serviceState")
                Log.d("testt", "serviceMessage: $serviceMessage")
                if (serviceState == "ON_SERVICE") {
                    Log.d("testt", "온서비스")
                } else {
                    Log.d("testt", "온서비스아님")
                }
            }
        }
    }

    private fun getFCMToken() {
        val prefs = getSharedPreferences("FCM_PREFS", Context.MODE_PRIVATE)
        val pendingToken = prefs.getString("fcm_token", null)
        val needsSync = prefs.getBoolean("token_needs_sync", false)

        if (pendingToken != null && needsSync) {
            FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val installationId = task.result
                        Log.d("FCM Token", "deviceId: $installationId")
                        Log.d("FCM Token", "Pending Token: $pendingToken")
                        userRepository.postToken(
                            fcmToken = pendingToken,
                            deviceId = installationId
                        ) { success, response ->
                            if (success) {
                                Log.d("FCM Token", "Token successfully posted to server")
                                prefs.edit().putBoolean("token_needs_sync", false).apply()
                            } else {
                                Log.e("FCM Token", "Failed to post token to server: ${response?.message}")
                            }
                        }
                    }
                }
        }
    }

    private fun initNavigationBar() {

        binding.bottomNavigationView.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> {
                        val fragment = TodayLogFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                        true
                    }

                    R.id.feed -> {
                        val fragment = ShareViewFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                        true
                    }

                    R.id.bottomCalendar -> {
                        val fragment = CalendarViewFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                        true
                    }

                    R.id.search -> {
                        val fragment = SearchFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                        true
                    }

                    R.id.settings -> {
                        val fragment = SettingFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit()
                        true
                    }

                    else -> false
                }
            }
        }
    }

}