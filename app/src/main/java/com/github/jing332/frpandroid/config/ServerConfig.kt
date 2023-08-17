package com.github.jing332.frpandroid.config

import com.funny.data_saver.core.DataSaverPreferences
import com.funny.data_saver.core.mutableDataSaverStateOf
import com.github.jing332.frpandroid.app

object ServerConfig {
    private val pref =
        DataSaverPreferences(app.getSharedPreferences("server", 0))


    var port = mutableDataSaverStateOf(
        dataSaverInterface = pref,
        key = "port",
        initialValue = 2344
    )

    /**
     * 单位 秒
     */
    var timeout = mutableDataSaverStateOf(
        dataSaverInterface = pref,
        key = "timeout",
        initialValue = 10
    )
}