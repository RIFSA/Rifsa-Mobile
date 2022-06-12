package com.example.rifsa_mobile.model.local.databaseconfig

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rifsa_mobile.model.entity.local.disase.Disease
import com.example.rifsa_mobile.model.local.dao.LocalDao

@Database(
    entities = [
        Disease::class
    ],
    version = 26, exportSchema = false
)
abstract class DatabaseConfig : RoomDatabase() {
    abstract fun localDao() : LocalDao

    companion object{
        @Volatile
        private var INSTANCE : DatabaseConfig? = null

        @JvmStatic
        fun getDatabase(context : Context): DatabaseConfig{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseConfig::class.java,"testDB"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }

    }


}