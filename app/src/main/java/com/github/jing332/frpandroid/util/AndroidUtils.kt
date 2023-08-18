package com.github.jing332.frpandroid.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat

object AndroidUtils {
    const val ABI_ARM = "armeabi-v7a"
    const val ABI_ARM64 = "arm64-v8a"
    const val ABI_X86 = "x86"
    const val ABI_X86_64 = "x86_64"

    fun getABI(): String? {
        return Build.SUPPORTED_ABIS[0]
    }

    fun Context.registerGlobalReceiver(
        receiver: BroadcastReceiver,
        filter: IntentFilter,
        flags: Int = ContextCompat.RECEIVER_EXPORTED
    ) {
        ContextCompat.registerReceiver(this, receiver, filter, flags)
    }

    fun Context.openUri(uri: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uri)
        startActivity(intent)
    }

}