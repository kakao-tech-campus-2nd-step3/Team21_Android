package potatocake.katecam.everymoment.services.location

import android.app.Application
import potatocake.katecam.everymoment.data.repository.PreferenceUtil
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "${potatocake.katecam.everymoment.BuildConfig.KAKAO_NATIVE_KEY}")
        prefs = PreferenceUtil(applicationContext)
    }
}