package com.github.jing332.frpandroid.model.frp.config

import android.os.FileObserver
import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.runBlocking
import java.io.File

class IniConfigManager {
    companion object {
        const val TAG = "IniConfigManager"
    }

    var iniFilePath: String = ""

    var cfg: IniConfig? = null

    private var mFileObserver: FileObserver? = null

    private fun checkPath() {
        if (iniFilePath.isBlank())
            throw IllegalArgumentException("iniFilePath is empty")
    }

    @Suppress("DEPRECATION")
    suspend fun flowConfig(): Flow<IniConfig> = coroutineScope {
        checkPath()
        return@coroutineScope channelFlow {
            fun sendConfig() {
                val str = File(iniFilePath).readText()
                cfg = IniConfigParser.load(str)
                runBlocking { send(cfg!!) }
            }

            mFileObserver = object : FileObserver(iniFilePath, ALL_EVENTS) {
                override fun onEvent(event: Int, path: String?) {
                    when (event) {
                        MODIFY -> {
                            Log.i(TAG, "MODIFY")
                            sendConfig()

                        }

                        CLOSE_WRITE -> {
                            Log.i(TAG, "CLOSE_WRITE")
                        }

                        CLOSE_NOWRITE -> {
                            Log.i(TAG, "CLOSE_NOWRITE")
                        }

                        MOVE_SELF -> {
                            Log.i(TAG, "MOVE_SELF")
                        }

                        MOVED_FROM -> {
                            Log.i(TAG, "MOVED_FROM")
                        }

                        OPEN -> {
                            Log.i(TAG, "OPEN")
                        }

                        ATTRIB -> {
                            Log.i(TAG, "ATTRIB")
                        }

                        ACCESS -> {
                            Log.i(TAG, "ACCESS")
                        }

                        else -> {
                            Log.i(TAG, "onEvent: $event")
                        }
                    }
                }
            }

            mFileObserver!!.startWatching()
            sendConfig()

            try {
                awaitCancellation()
            } catch (e: CancellationException) {
                Log.i(TAG, "flowConfig: stopWatching() ...")
            }
            mFileObserver?.stopWatching()
        }
    }


    fun edit(section: String, key: String, value: String) {
        Log.i(TAG, "edit: section=$section, key=$key, newValue=$value")
        checkPath()
        var iniString = File(iniFilePath).readText()
        iniString = IniConfigParser.edit(iniString, section, key, value)
        dumpToFile(iniString)
    }

    fun dumpToFile(str: String) {
        checkPath()

        backupIniFile()
        val file = File(iniFilePath)
        file.writeText(str)
    }

    private fun backupIniFile() {
        val file = File(iniFilePath)
        val backupFile = File("$iniFilePath.bak")
        file.copyTo(backupFile, true)
    }
}