package com.example.everymoment.presentation.view.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.everymoment.R
import com.example.everymoment.databinding.ActivityMainBinding
import com.example.everymoment.presentation.view.main.search.SearchFragment
import com.example.everymoment.presentation.view.sub.PostFragment
import com.example.everymoment.presentation.view.sub.diary.DiaryEditFragment
import com.example.everymoment.presentation.view.sub.diary.DiaryReadFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                        val fragment = DiaryReadFragment()
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