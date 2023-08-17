package com.github.jing332.frpandroid.config

import com.funny.data_saver.core.DataSaverPreferences
import com.funny.data_saver.core.mutableDataSaverStateOf
import com.github.jing332.frpandroid.app

object AppConfig {
    private val pref =
        DataSaverPreferences(app.getSharedPreferences("app", 0))


    var isAutoCheckUpdate = mutableDataSaverStateOf(
        dataSaverInterface = pref,
        key = "isCheckUpdate",
        initialValue = true
    )

}