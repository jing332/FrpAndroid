package com.github.jing332.frpandroid.model.frp

import android.content.Context
import java.io.File

class Frpc(val context: Context) :
    Frp(context.applicationInfo.nativeLibraryDir + File.separator + "libfrpc.so") {


    override fun getConfigFilePath(): String =
        "${context.getFrpDir()}/frpc.ini"

    private var mProcess: Process? = null
    val process: Process?
        get() = mProcess

    fun startup() {
        mProcess = start()
    }

    fun shutdown() {
        mProcess?.destroy()
    }
}