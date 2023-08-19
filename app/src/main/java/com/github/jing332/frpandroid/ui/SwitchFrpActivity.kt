package com.github.jing332.frpandroid.ui

import android.app.Activity
import android.os.Bundle
import com.github.jing332.frpandroid.service.FrpServiceManager.frpcSwitch
import com.github.jing332.frpandroid.util.ToastUtils.longToast

class SwitchFrpActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (intent.action) {
            "frpc" -> {
                frpcSwitch()
            }

            "frps" -> {
                frpcSwitch()
            }

            else -> {
                longToast("未知action: ${intent.action}")
            }
        }
        finish()
    }
}