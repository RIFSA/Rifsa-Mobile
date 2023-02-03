package com.example.rifsa_mobile.model.local.room.dbconfig

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.model.local.room.dao.DiseaseDao
import com.example.rifsa_mobile.model.local.room.dao.FinancialDao
import com.example.rifsa_mobile.model.local.room.dao.HarvestDao

@Database(
    entities = [
        HarvestEntity::class,
        FinancialEntity::class,
        DiseaseEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DatabaseConfig: RoomDatabase() {
    abstract fun diseaseDao(): DiseaseDao
    abstract fun harvestDao(): HarvestDao
    abstract fun financialDao(): FinancialDao

    companion object{
        @Volatile
        private var INSTANCE : DatabaseConfig? = null

        @JvmStatic
        fun setDatabase(context : Context): DatabaseConfig{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseConfig::class.java,"RifsaLocalDB"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}