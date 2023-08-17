package com.github.jing332.frpandroid.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.drake.net.utils.runMain

object ToastUtils {
    fun Context.toast(@StringRes strId: String) {
        runMain {
            Toast.makeText(this, strId, Toast.LENGTH_SHORT).show()
        }
    }

    fun Context.toast(@StringRes strId: Int, vararg args: Any) {
        runMain {
            Toast.makeText(
                this,
                getString(strId, *args),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun Context.longToast(str: String) {
        runMain {
            Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        }
    }

    fun Context.longToast(@StringRes strId: Int) {
        runMain {
            Toast.makeText(this, strId, Toast.LENGTH_LONG).show()
        }
    }

    fun Context.longToast(@StringRes strId: Int, vararg args: Any) {
        runMain {
            Toast.makeText(
                this,
                getString(strId, *args),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}