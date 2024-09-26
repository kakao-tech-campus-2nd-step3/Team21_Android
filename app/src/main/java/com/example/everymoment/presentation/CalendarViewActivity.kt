package com.example.everymoment.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.everymoment.R
import com.kakao.sdk.common.util.Utility

class CalendarViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        Log.d("testt", "keyhash : ${Utility.getKeyHash(this)}")
    }
}