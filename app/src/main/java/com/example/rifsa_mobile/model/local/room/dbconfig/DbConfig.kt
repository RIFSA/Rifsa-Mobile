package com.example.rifsa_mobile.model.local.room.dbconfig

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rifsa_mobile.model.entity.local.disease.DiseaseLocal
import com.example.rifsa_mobile.model.local.room.dao.DiseaseDao

@Database(
    entities = [
        DiseaseLocal::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DbConfig: RoomDatabase() {
    abstract fun diseaseDao(): DiseaseDao

    companion object{
        @Volatile
        private var INSTANCE : DbConfig? = null

        @JvmStatic
        fun setDatabase(context : Context): DbConfig{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DbConfig::class.java,"RifsaLocalDB"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}