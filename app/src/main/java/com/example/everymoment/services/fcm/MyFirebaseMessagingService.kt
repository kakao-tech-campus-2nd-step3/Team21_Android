package com.example.everymoment.services.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.everymoment.services.notification.NotificationActionReceiver
import com.example.everymoment.R
import com.example.everymoment.data.model.entity.Emotions
import com.example.everymoment.presentation.view.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", "New token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("testt", "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d("testt", "Message Notification Body: ${it.body}")

            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            createNotificationChannel()
            setNotification()
        }
    }

    private fun setNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val remoteViews = RemoteViews(packageName, R.layout.custom_notification)
        remoteViews.setTextViewText(R.id.happyEmojiTextView, Emotions.HAPPY.getEmotionUnicode())
        remoteViews.setTextViewText(R.id.sadEmojiTextView, Emotions.SAD.getEmotionUnicode())
        remoteViews.setTextViewText(R.id.insensitiveEmojiTextView, Emotions.INSENSITIVE.getEmotionUnicode())
        remoteViews.setTextViewText(R.id.angryEmojiTextView, Emotions.ANGRY.getEmotionUnicode())
        remoteViews.setTextViewText(R.id.confoundedEmojiTextView, Emotions.CONFOUNDED.getEmotionUnicode())

        val emotions = listOf(
            R.id.happyEmojiTextView to Emotions.HAPPY,
            R.id.sadEmojiTextView to Emotions.SAD,
            R.id.insensitiveEmojiTextView to Emotions.INSENSITIVE,
            R.id.angryEmojiTextView to Emotions.ANGRY,
            R.id.confoundedEmojiTextView to Emotions.CONFOUNDED
        )

        emotions.forEach { (viewId, emotion) ->
            val emotionIntent = Intent(this, NotificationActionReceiver::class.java).apply {
                action = "${emotion.name}_ACTION"
            }
            val emotionPendingIntent = PendingIntent.getBroadcast(
                this,
                viewId,
                emotionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            remoteViews.setOnClickPendingIntent(viewId, emotionPendingIntent)
        }

        val builder = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("EveryMoment")
            .setContentIntent(pendingIntent)
            .setContentText("현재 XX 위치에 머무르고 있어요! 지금의 기분은 어떠신가요?")
            .setCustomBigContentView(remoteViews)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel() {
        val descriptionText = getString(R.string.fcm_channel_description)
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_ID = 222222
        private const val CHANNEL_ID = "main_default_channel"
        private const val CHANNEL_NAME = "main channelName"
    }
}