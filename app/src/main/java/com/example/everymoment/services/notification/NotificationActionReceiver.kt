package com.example.everymoment.services.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.everymoment.data.model.entity.Emotions
import com.example.everymoment.data.model.network.api.NetworkModule
import com.example.everymoment.data.model.network.api.PotatoCakeApiService
import com.example.everymoment.data.model.network.dto.response.ServerResponse
import com.example.everymoment.services.location.GlobalApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActionReceiver : BroadcastReceiver() {

    private val apiService: PotatoCakeApiService =
        NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token = "Bearer $jwtToken"
    override fun onReceive(context: Context, intent: Intent) {
        val emotion = when (intent.action) {
            "${Emotions.HAPPY.name}_ACTION" -> Emotions.HAPPY
            "${Emotions.SAD.name}_ACTION" -> Emotions.SAD
            "${Emotions.INSENSITIVE.name}_ACTION" -> Emotions.INSENSITIVE
            "${Emotions.ANGRY.name}_ACTION" -> Emotions.ANGRY
            "${Emotions.CONFOUNDED.name}_ACTION" -> Emotions.CONFOUNDED
            else -> null
        }

        emotion?.let { handleEmotion(context, it) }
    }

    private fun handleEmotion(context: Context, emotion: Emotions) {
        val emoji = emotion.getEmotionUnicode()
        val label = when (emotion) {
            Emotions.HAPPY -> "행복"
            Emotions.SAD -> "슬픔"
            Emotions.INSENSITIVE -> "무표정"
            Emotions.ANGRY -> "화남"
            Emotions.CONFOUNDED -> "싫음"
        }

        updateEmojiStatus(49) { success, message ->
            val toastMessage = if (success) {
                "감정이 업데이트되었습니다: $emoji $label"
            } else {
                "감정 업데이트 실패: ${message ?: "알 수 없는 오류"}"
            }
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(context, "선택된 감정: $emoji $label", Toast.LENGTH_SHORT).show()
    }

    fun updateEmojiStatus(
        diaryId: Int,
        callback: (Boolean, String?) -> Unit
    ) {
        apiService.updateEmojiStatus(token, diaryId).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful) {
                    Log.d("SendEmoji", "${p1.body()}")
                    callback(true, p1.message())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
                Log.d("SendEmoji", "Failed to send emoji: ${p1.message}")
                callback(false, null)
            }
        })
    }
}