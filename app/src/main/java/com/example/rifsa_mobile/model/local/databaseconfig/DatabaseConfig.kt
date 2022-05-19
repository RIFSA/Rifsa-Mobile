package com.example.rifsa_mobile.model.local.databaseconfig

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rifsa_mobile.model.entity.finance.Finance
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.inventory.Inventory
import com.example.rifsa_mobile.model.local.dao.LocalDao

@Database(
    entities = [
        HarvestResult::class,
        Finance::class,
        Inventory::class
    ],
    version = 4, exportSchema = false
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