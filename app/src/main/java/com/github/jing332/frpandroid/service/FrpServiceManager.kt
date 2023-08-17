package com.github.jing332.frpandroid.service

import android.content.Context
import android.content.Intent

object FrpServiceManager {

    fun Context.frpcSwitch() {
        if (frpcRunning()) {
            shutdownFrpc()
        } else {
            startFrpc()
        }
    }

    fun Context.frpsSwitch() {
        if (frpsRunning()) {
            shutdownFrps()
        } else {
            startFrps()
        }
    }

    fun Context.startFrpc() {
        startService(Intent(this, FrpcService::class.java))
    }

    fun Context.shutdownFrpc() {
        startService(Intent(this, FrpcService::class.java).apply {
            action = FrpcService.ACTION_SHUTDOWN
        })
    }

    fun Context.startFrps() {
        startService(Intent(this, FrpsService::class.java))
    }

    fun Context.shutdownFrps() {
        startService(Intent(this, FrpsService::class.java).apply {
            action = FrpsService.ACTION_SHUTDOWN
        })
    }

    fun frpcRunning(): Boolean = FrpcService.frpcRunning
    fun frpsRunning(): Boolean = FrpsService.frpRunning
}