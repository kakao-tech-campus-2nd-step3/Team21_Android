package com.example.everymoment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.everymoment.data.model.Emotions

class NotificationActionReceiver : BroadcastReceiver() {
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

        // 선택된 감정 처리 -> 백엔드 전송 필요
        Toast.makeText(context, "선택된 감정: $emoji $label", Toast.LENGTH_SHORT).show()
    }
}