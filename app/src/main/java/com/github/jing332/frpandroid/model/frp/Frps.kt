package com.github.jing332.frpandroid.model.frp

import android.content.Context
import java.io.File

class Frps(val context: Context) :
    Frp(context.applicationInfo.nativeLibraryDir + File.separator + "libfrps.so") {
    override fun getConfigFilePath(): String =
        "${context.getExternalFilesDir("frp")!!.absolutePath}/frps.ini"

}