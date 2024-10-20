package com.example.everymoment.services.location

import android.app.Application
import com.example.everymoment.BuildConfig
import com.example.everymoment.data.repository.PreferenceUtil
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "${BuildConfig.KAKAO_NATIVE_KEY}")
        prefs = PreferenceUtil(applicationContext)
    }
}