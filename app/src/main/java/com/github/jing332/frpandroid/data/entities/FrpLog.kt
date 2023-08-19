package com.github.jing332.frpandroid.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.jing332.frpandroid.constant.FrpType
import com.github.jing332.frpandroid.constant.LogLevel

@Entity("frp_logs")
data class FrpLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val type: FrpType,
    @LogLevel val level: Int,
    val message: String,
    val description: String? = null,
) {

}