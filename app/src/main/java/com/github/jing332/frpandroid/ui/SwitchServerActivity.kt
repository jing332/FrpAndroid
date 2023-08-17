package com.github.jing332.frpandroid.ui

import android.app.Activity
import android.os.Bundle

class SwitchServerActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (AList.hasRunning) {
//            toast(R.string.alist_shut_downing)
//            startService(Intent(this, FrpService::class.java).apply {
//                action = FrpService.ACTION_SHUTDOWN
//            })
//        } else {
//            toast(R.string.alist_starting)
//            startService(Intent(this, FrpService::class.java))
//        }

        finish()
    }
}