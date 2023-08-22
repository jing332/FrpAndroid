package com.github.jing332.frpandroid.config

import com.funny.data_saver.core.DataSaverConverter
import com.funny.data_saver.core.DataSaverPreferences
import com.funny.data_saver.core.mutableDataSaverStateOf
import com.github.jing332.frpandroid.app
import com.github.jing332.frpandroid.constant.FrpType
import java.io.File

object AppConfig {
    private val pref =
        DataSaverPreferences(app.getSharedPreferences("app", 0))

    val isFirstRun: Boolean
        get() = File(
            (app.filesDir.parentFile?.absolutePath + File.separator + "shared_prefs")
        ).exists().not()

    init {
        DataSaverConverter.registerTypeConverters<FrpType>(
            { it.name },
            { FrpType.valueOf(it) }
        )
    }

    var isAutoCheckUpdate = mutableDataSaverStateOf(
        dataSaverInterface = pref,
        key = "isCheckUpdate",
        initialValue = true
    )

    var frpPageType = mutableDataSaverStateOf<FrpType>(
        dataSaverInterface = pref,
        key = "frpType",
        initialValue = FrpType.FRPC
    )

    var subPageIndex = mutableDataSaverStateOf(
        dataSaverInterface = pref,
        key = "pageIndex",
        initialValue = 0
    )

    val enabledWakeLock = mutableDataSaverStateOf(
        dataSaverInterface = pref,
        key = "enabledWakeLock",
        initialValue = false
    )


}
