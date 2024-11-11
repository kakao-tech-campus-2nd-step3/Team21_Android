package potatocake.katecam.everymoment.services.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import potatocake.katecam.everymoment.data.model.network.api.NetworkModule
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.request.EmojiRequest
import potatocake.katecam.everymoment.data.model.network.dto.response.ServerResponse
import potatocake.katecam.everymoment.services.location.GlobalApplication
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
            "${potatocake.katecam.everymoment.data.model.entity.Emotions.HAPPY.name}_ACTION" -> potatocake.katecam.everymoment.data.model.entity.Emotions.HAPPY
            "${potatocake.katecam.everymoment.data.model.entity.Emotions.SAD.name}_ACTION" -> potatocake.katecam.everymoment.data.model.entity.Emotions.SAD
            "${potatocake.katecam.everymoment.data.model.entity.Emotions.INSENSITIVE.name}_ACTION" -> potatocake.katecam.everymoment.data.model.entity.Emotions.INSENSITIVE
            "${potatocake.katecam.everymoment.data.model.entity.Emotions.ANGRY.name}_ACTION" -> potatocake.katecam.everymoment.data.model.entity.Emotions.ANGRY
            "${potatocake.katecam.everymoment.data.model.entity.Emotions.CONFOUNDED.name}_ACTION" -> potatocake.katecam.everymoment.data.model.entity.Emotions.CONFOUNDED
            else -> null
        }

        emotion?.let { handleEmotion(context, it) }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(EMOJI_NOTIFICATION_ID)
    }

    private fun handleEmotion(context: Context, emotion: potatocake.katecam.everymoment.data.model.entity.Emotions) {
        val emoji = when (emotion) {
            potatocake.katecam.everymoment.data.model.entity.Emotions.HAPPY -> "happy"
            potatocake.katecam.everymoment.data.model.entity.Emotions.SAD -> "sad"
            potatocake.katecam.everymoment.data.model.entity.Emotions.INSENSITIVE -> "insensitive"
            potatocake.katecam.everymoment.data.model.entity.Emotions.ANGRY -> "angry"
            potatocake.katecam.everymoment.data.model.entity.Emotions.CONFOUNDED -> "confounded"
        }
        val label = when (emotion) {
            potatocake.katecam.everymoment.data.model.entity.Emotions.HAPPY -> "행복"
            potatocake.katecam.everymoment.data.model.entity.Emotions.SAD -> "슬픔"
            potatocake.katecam.everymoment.data.model.entity.Emotions.INSENSITIVE -> "무표정"
            potatocake.katecam.everymoment.data.model.entity.Emotions.ANGRY -> "화남"
            potatocake.katecam.everymoment.data.model.entity.Emotions.CONFOUNDED -> "싫음"
        }

        updateEmojiStatus(30, emoji) { success, message ->
            val toastMessage = if (success) {
                "감정이 업데이트되었습니다: $emoji $label"
            } else {
                "감정 업데이트 실패: ${message ?: "알 수 없는 오류"}"
            }
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(context, "선택된 감정: $emoji $label", Toast.LENGTH_SHORT).show()
    }

    private fun updateEmojiStatus(
        diaryId: Int,
        emoji: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val emojiRequest = EmojiRequest(emoji = emoji)

        apiService.updateEmojiStatus(token, diaryId, emojiRequest)
            .enqueue(object : Callback<ServerResponse> {
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

    companion object {
        private const val EMOJI_NOTIFICATION_ID = 222222
    }
}