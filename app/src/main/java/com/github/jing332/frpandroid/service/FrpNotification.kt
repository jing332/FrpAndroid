package com.github.jing332.frpandroid.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.ui.theme.androidColor

@Suppress("DEPRECATION")
object FrpNotification {
    fun contentPaddingFlag(): Int {
        // Android 12(S)+ 必须指定PendingIntent.FLAG_
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.FLAG_IMMUTABLE
        else
            0
    }

    fun Context.createNotification(
        title: String,
        contentText: String,
        icon: Int,
        chanelId: String,

        contentAction: PendingIntent,
        shutdownAction: PendingIntent,
    ): Notification {

        val color = com.github.jing332.frpandroid.ui.theme.seed.androidColor
        val smallIconRes: Int = icon
        val builder = Notification.Builder(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {/*Android 8.0+ 要求必须设置通知信道*/
            val chan = NotificationChannel(
                chanelId,
                getString(R.string.frp_server),
                NotificationManager.IMPORTANCE_NONE
            )
            chan.lightColor = color
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)
            builder.setChannelId(chanelId)
        } else {
        }

        return builder
            .setColor(color)
            .setContentTitle(title)
            .setContentText(contentText)
            .setSmallIcon(smallIconRes)
            .setContentIntent(contentAction)
            .addAction(0, getString(R.string.shutdown), shutdownAction)
            .build()
    }
}