package com.github.jing332.frpandroid.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.jing332.frpandroid.data.entities.FrpLog
import kotlinx.coroutines.flow.Flow

@Dao
interface FrpLogDao {
    @Query("SELECT * FROM frp_logs WHERE type = :type")
    fun flowAll(type: FrpLog.Type): Flow<List<FrpLog>>

    @Query("SELECT * FROM frp_logs WHERE type = :type")
    fun all(type: FrpLog.Type): List<FrpLog>

    @Query("DELETE FROM frp_logs WHERE type = :type")
    fun deleteAll(type: FrpLog.Type)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg log: FrpLog)
}