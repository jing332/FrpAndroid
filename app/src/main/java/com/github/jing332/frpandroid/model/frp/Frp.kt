package com.github.jing332.frpandroid.model.frp

import android.content.Context
import com.github.jing332.frpandroid.util.FileUtils.readAllText
import java.io.IOException


abstract class Frp(open val execPath: String) {
    companion object {
        const val TAG = "Frp"
        const val RET_OK = 0

        fun Context.getFrpDir(): String = getExternalFilesDir("frp")!!.absolutePath
    }


    abstract fun getConfigFilePath(): String

    fun start(configFilePath: String = getConfigFilePath()): Process {
        return execWithParams(params = arrayOf("-c", configFilePath))
    }

    /**
     * @return 空字符串表示成功，否则返回错误信息
     */
    open fun verify(configFilePath: String): String {
        val p = execWithParams(params = arrayOf("verify", "-c", configFilePath))
        val ret = p.waitFor()

        return if (ret == RET_OK)
            ""
        else
            p.errorStream.readAllText()
    }

    fun version(): String {
        val p = execWithParams(params = arrayOf("-v"))
        p.waitFor()
        p.inputStream?.let {
            it.bufferedReader().use { buffered ->
                return buffered.readLine()
            }
        }

        return ""
    }

    @Throws(IOException::class)
    fun execWithParams(
        redirect: Boolean = false,
        vararg params: String
    ): Process {
        val cmdline = arrayOfNulls<String>(params.size + 1)
        cmdline[0] = execPath
        System.arraycopy(params, 0, cmdline, 1, params.size)
        return ProcessBuilder(*cmdline).redirectErrorStream(redirect).start()
            ?: throw IOException("Process is null!")
    }
}