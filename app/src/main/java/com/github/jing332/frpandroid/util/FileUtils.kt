package com.github.jing332.frpandroid.util

import java.io.File
import java.io.InputStream
import java.net.URLConnection

object FileUtils {
    val File.mimeType: String?
        get() {
            val fileNameMap = URLConnection.getFileNameMap()
            return fileNameMap.getContentTypeFor(name)
        }

    /**
     * 按行读取txt
     */
    fun InputStream.readAllText(): String {
        this.use { ins ->
            ins.bufferedReader().use { reader ->
                val buffer = StringBuffer("")
                var str: String?
                while (reader.readLine().also { str = it } != null) {
                    buffer.append(str)
                    buffer.append("\n")
                }
                return buffer.toString()
            }
        }
    }

    fun copyFolder(src: File, target: File, overwrite: Boolean = true) {
        val folder = File(target.absolutePath + File.separator + src.name)
        folder.mkdirs()

        src.listFiles()?.forEach {
            if (it.isFile) {
                val newFile = File(folder.absolutePath + File.separator + it.name)
                it.copyTo(newFile, overwrite)
            } else if (it.isDirectory) {
                copyFolder(it, folder)
            }
        }

    }
}