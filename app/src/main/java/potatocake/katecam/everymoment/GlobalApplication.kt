package potatocake.katecam.everymoment

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import potatocake.katecam.everymoment.data.repository.PreferenceUtil

@HiltAndroidApp
class GlobalApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
        private const val PREF_FIRST_INSTALL = "first_install"
    }
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "${BuildConfig.KAKAO_NATIVE_KEY}")
        prefs = PreferenceUtil(applicationContext)

        if (!prefs.getBoolean(PREF_FIRST_INSTALL, false)) {
            prefs.setBoolean(PREF_FIRST_INSTALL, true)
            prefs.setBoolean("isAutoNotificationEnabled", true)
        }
    }
}