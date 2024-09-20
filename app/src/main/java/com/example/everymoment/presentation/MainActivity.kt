package com.example.everymoment.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.everymoment.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DiaryReadFragment())
                .commit()
        }
    }
}