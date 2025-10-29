package com.chikegam.henwoldir.fergok.presentation.notificiation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.chikegam.henwoldir.ChickHenWorldActivity
import com.chikegam.henwoldir.R
import com.chikegam.henwoldir.fergok.presentation.app.ChickHenWorldApp
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val CHICK_HEN_WORLD_CHANNEL_ID = "chick_hen_world_notifications"
private const val CHICK_HEN_WORLD_CHANNEL_NAME = "Chick Hen World Notifications"
private const val CHICK_HEN_WORLD_NOT_TAG = "ChickHenWorld"

class ChickHenWorldPushService : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Обработка notification payload
        remoteMessage.notification?.let {
            if (remoteMessage.data.contains("url")) {
                chickHenWorldShowNotification(it.title ?: CHICK_HEN_WORLD_NOT_TAG, it.body ?: "", data = remoteMessage.data["url"])
            } else {
                chickHenWorldShowNotification(it.title ?: CHICK_HEN_WORLD_NOT_TAG, it.body ?: "", data = null)
            }
        }

        // Обработка data payload
        if (remoteMessage.data.isNotEmpty()) {
            chickHenWorldHandleDataPayload(remoteMessage.data)
        }
    }

    private fun chickHenWorldShowNotification(title: String, message: String, data: String?) {
        val chickHenWorldNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Создаем канал уведомлений для Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHICK_HEN_WORLD_CHANNEL_ID,
                CHICK_HEN_WORLD_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            chickHenWorldNotificationManager.createNotificationChannel(channel)
        }

        val chickHenWorldIntent = Intent(this, ChickHenWorldActivity::class.java).apply {
            putExtras(bundleOf(
                "url" to data
            ))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val chickHenWorldPendingIntent = PendingIntent.getActivity(
            this,
            0,
            chickHenWorldIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val chickHenWorldNotification = NotificationCompat.Builder(this, CHICK_HEN_WORLD_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_chick_hen_world_noti)
            .setAutoCancel(true)
            .setContentIntent(chickHenWorldPendingIntent)
            .build()

        chickHenWorldNotificationManager.notify(System.currentTimeMillis().toInt(), chickHenWorldNotification)
    }

    private fun chickHenWorldHandleDataPayload(data: Map<String, String>) {
        data.forEach { (key, value) ->
            Log.d(ChickHenWorldApp.CHICK_HEN_WORLD_MAIN_TAG, "Data key=$key value=$value")
        }
    }
}