package com.github.jing332.frpandroid.model.frp

import com.github.jing332.frpandroid.constant.LogLevel

object LogUtils {
    @Suppress("RegExpRedundantEscape")
    fun String.evalLog(): Log? {
        val regexPattern =
            Regex("""(\d{4}\/\d{2}\/\d{2} \d{2}:\d{2}:\d{2}) \[(\w+)\] \[(\w+.\w+:\d+)\] (.*)""")

        val matchResult = regexPattern.find(this)
        return matchResult?.let { result ->
            val time = result.groupValues[1]
            val level = result.groupValues[2]
            val code = result.groupValues[3]
            val msg = result.groupValues[4]

            val l = when (level) {
                "D" -> LogLevel.DEBUG
                "I" -> LogLevel.INFO
                "W" -> LogLevel.WARN
                "E" -> LogLevel.ERROR
                else -> LogLevel.INFO
            }
            return Log(time, l, code, msg)
        }
    }

    data class Log(val time: String, @LogLevel val level: Int, val code: String, val msg: String)
}