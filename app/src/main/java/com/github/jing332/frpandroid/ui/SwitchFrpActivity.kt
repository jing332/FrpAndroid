package com.github.jing332.frpandroid.ui

import android.app.Activity
import android.os.Bundle
import com.github.jing332.frpandroid.service.FrpServiceManager.frpcSwitch
import com.github.jing332.frpandroid.util.ToastUtils.longToast
import com.github.jing332.frpandroid.util.ToastUtils.toast

class SwitchFrpActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        switch((intent.type ?: "").ifBlank { intent.action ?: "" })

        finish()
    }

    private fun switch(type: String) {
        toast("$type 启动中")
        when (type) {
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
    }
}