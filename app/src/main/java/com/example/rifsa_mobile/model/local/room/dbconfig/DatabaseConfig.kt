package com.example.rifsa_mobile.model.local.room.dbconfig

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.model.local.room.LoadDummyJson
import com.example.rifsa_mobile.model.local.room.dao.DiseaseDao
import com.example.rifsa_mobile.model.local.room.dao.FinancialDao
import com.example.rifsa_mobile.model.local.room.dao.HarvestDao
import com.example.rifsa_mobile.model.repository.local.mediator.RemoteKeys
import java.util.concurrent.Executors

@Database(
    entities = [
        HarvestEntity::class,
        FinancialEntity::class,
        DiseaseEntity::class,
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
                ).addCallback(object: Callback (){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadExecutor().execute {
                            fillDataFinancial(
                                context,
                                setDatabase(context).financialDao()
                            )
                            fillDataHarvest(
                                context,
                                setDatabase(context).harvestDao()
                            )
                        }
                    }
                })
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { INSTANCE = it }
            }
        }

        //prefill dummy data
        private fun fillDataFinancial(
            context: Context,
            financialDao : FinancialDao
        ){
            val financialJsonArray = LoadDummyJson.loadFinancialjson(context)

            try {
                if (financialJsonArray != null){
                    for (i in 0 until financialJsonArray.length()){
                        val item = financialJsonArray.getJSONObject(i)
                        financialDao.insertFinanceLocally(FinancialEntity(
                            localId = item.getInt("localId"),
                            firebaseUserId = item.getString("firebaseUserId"),
                            idFinance = item.getString("idFinance"),
                            date = item.getString("date"),
                            day = item.getInt("day"),
                            month = item.getInt("month"),
                            year = item.getInt("year"),
                            name = item.getString("name"),
                            type = item.getString("type"),
                            noted = item.getString("noted"),
                            isUploaded = item.getBoolean("isUploaded"),
                            uploadReminderId = item.getInt("uploadReminderId")
                        ))
                    }
                }
            }catch (e : Error){
                Log.d("databaseConfig",e.message.toString())
            }
        }

        private fun fillDataHarvest(
            context: Context,
            harvestDao : HarvestDao
        ){
            val harvest = LoadDummyJson.loadHarvestjson(context)

            try {
                if (harvest != null){
                    for (i in 0 until harvest.length()){
                        val item = harvest.getJSONObject(i)
                        harvestDao.insertHarvestLocally(
                            HarvestEntity(
                            localId = item.getInt("localId"),
                            id = item.getString("id"),
                            firebaseUserId = item.getString("firebaseUserId"),
                            date = item.getString("date"),
                            day = item.getInt("day"),
                            month = item.getInt("month"),
                            year = item.getInt("year"),
                            typeOfGrain = item.getString("typeOfGrain"),
                            weight= item.getInt("weight"),
                            income = item.getInt("income"),
                            note = item.getString("note"),
                            isUploaded = item.getBoolean("isUploaded"),
                            uploadReminderId = item.getInt("uploadReminderId")
                        ))
                    }
                }
            }catch (e : Error){
                Log.d("databaseConfig",e.message.toString())
            }
        }
    }
}