package com.example.everymoment.presentation.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.everymoment.R
import com.example.everymoment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                        val fragment = TodayLogFragment()
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