package com.example.everymoment

import android.app.Application
import com.example.everymoment.data.model.PreferenceUtil
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