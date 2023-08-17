package com.github.jing332.frpandroid.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.jing332.frpandroid.app
import com.github.jing332.frpandroid.data.dao.FrpLogDao
import com.github.jing332.frpandroid.data.entities.FrpLog

val appDb by lazy { AppDatabase.create() }

@Database(
    version = 1,
    entities = [FrpLog::class],
    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val frpLogDao: FrpLogDao

    companion object {
        fun create() = Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "frpandroid.db"
        )
            .allowMainThreadQueries()
            .build()
    }
}
