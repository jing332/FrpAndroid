package com.github.jing332.frpandroid.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.github.jing332.frpandroid.R
import com.github.jing332.frpandroid.constant.AppConst
import com.github.jing332.frpandroid.data.appDb
import com.github.jing332.frpandroid.data.entities.FrpLog
import com.github.jing332.frpandroid.model.frp.Frp
import com.github.jing332.frpandroid.model.frp.LogUtils.evalLog
import com.github.jing332.frpandroid.util.AndroidUtils.registerGlobalReceiver
import com.github.jing332.frpandroid.util.StringUtils.removeAnsiCodes
import com.github.jing332.frpandroid.util.ToastUtils.longToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

abstract class FrpService(
    val type: FrpLog.Type,
    val shutdownAction: String,
    val onStatusChangedAction: String
) :
    Service() {
    companion object {
        const val TAG = "FrpService"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    var process: Process? = null
    open var isRunning: Boolean = false

    abstract val frp: Frp

    private fun statusChanged() {
        AppConst.localBroadcast.sendBroadcast(Intent(onStatusChangedAction))
    }

    private suspend fun errorLogWatcher(onNewLine: (String) -> Unit) {
        process?.apply {
            errorStream.bufferedReader().use {
                while (coroutineContext.isActive) {
                    val line = it.readLine() ?: break
                    onNewLine(line)
                }
            }
        }
    }

    private suspend fun logWatcher(onNewLine: (String) -> Unit) {
        process?.apply {
            inputStream.bufferedReader().use {
                while (coroutineContext.isActive) {
                    val line = it.readLine() ?: break
                    onNewLine(line)
                }
            }
        }
    }


    val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private fun initOutput() {
        val dao = appDb.frpLogDao
        coroutineScope.launch {
            logWatcher { msg ->
                val log = msg.removeAnsiCodes().evalLog() ?: return@logWatcher
                dao.insert(
                    FrpLog(
                        type = type,
                        level = log.level,
                        message = log.msg
                    )
                )
            }
        }
        coroutineScope.launch {
            errorLogWatcher { msg ->
                val log = msg.removeAnsiCodes().evalLog() ?: return@errorLogWatcher
                dao.insert(
                    FrpLog(
                        type = type,
                        level = log.level,
                        message = log.msg,
                        description = log.time + "\n" + log.code
                    )
                )
            }
        }
    }

    /**
     * @return 0表示正常退出，否则表示失败
     */
    @Synchronized
    fun startup(): Int {
        Log.i(TAG, "startup ... ")

        process = frp.start()
        initOutput()
        isRunning = true
        statusChanged()
        return process!!.waitFor().apply {
            Log.i(TAG, "shutdown done")
            isRunning = false
            statusChanged()
            stopSelf()
        }
    }

    /**
     * @return 退出进程并等待
     */
    suspend fun shutdown() {
        Log.i(TAG, "shutdown ...")
        process?.destroy()

        while (coroutineContext.isActive && isRunning) {
            delay(100)
        }
    }

    private val mNotificationReceiver = MyReceiver()
    override fun onCreate() {
        super.onCreate()
        registerGlobalReceiver(
            mNotificationReceiver,
            IntentFilter(shutdownAction),
        )
        appDb.frpLogDao.deleteAll(type)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            if (intent?.action == shutdownAction) {
                shutdown()
            } else {
                val ret = startup()
                if (ret == Frp.RET_OK) {
                } else {
                    longToast(R.string.frp_error)
                }
            }
        }


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(mNotificationReceiver)
    }

    inner class MyReceiver() : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                shutdownAction -> {
                    coroutineScope.launch {
                        shutdown()
                    }
                }
            }
        }
    }

}