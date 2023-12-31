package com.github.jing332.frpandroid.service

import android.app.PendingIntent
import android.content.Intent
import android.os.IBinder
import com.github.jing332.frpandroid.BuildConfig
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.constant.FrpType
import com.github.jing332.frpandroid.model.frp.Frp
import com.github.jing332.frpandroid.model.frp.Frpc
import com.github.jing332.frpandroid.service.FrpNotification.createNotification
import com.github.jing332.frpandroid.ui.MainActivity

class FrpcService(
) : FrpService(FrpType.FRPC, ACTION_SHUTDOWN, ACTION_STATUS_CHANGED) {
    companion object {

        const val TAG = "FrpcService"

        const val FOREGROUND_ID = 7000
        const val ACTION_SHUTDOWN =
            "${BuildConfig.APPLICATION_ID}.service.FrpcService.SHUTDOWN"

        const val ACTION_STATUS_CHANGED =
            "${BuildConfig.APPLICATION_ID}.service.FrpcService.ACTION_STATUS_CHANGED"

        var frpcRunning: Boolean = false
    }

    override var isRunning: Boolean
        get() = super.isRunning
        set(value) {
            super.isRunning = value
            frpcRunning = value
        }

    private val mFrp by lazy {
        Frpc(this)
    }

    override val frp: Frp
        get() = mFrp

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()

        createNotification(
            title = "Frpc",
            contentText = "FrpAndroid",
            icon = R.drawable.ic_notification_frpc,
            chanelId = "frpc",
            contentAction = PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                FrpNotification.contentPaddingFlag()
            ),
            shutdownAction = PendingIntent.getBroadcast(
                this,
                0,
                Intent(ACTION_SHUTDOWN),
                FrpNotification.contentPaddingFlag()
            )
        ).also {
            startForeground(FOREGROUND_ID, it)
        }
    }


}