package com.example.rifsa_mobile.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.rifsa_mobile.model.entity.finance.Finance
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.local.dao.LocalDao
import com.example.rifsa_mobile.model.local.databaseconfig.DatabaseConfig
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainRepository(
    database : DatabaseConfig,
    private val userPrefrences: UserPrefrences
) {

    private val dao = database.localDao()

    suspend fun insertLocalHarvest(data : HarvestResult){
        dao.insertHarvest(data)
    }

    suspend fun deleteLocalHarvest(id : String){
        dao.deleteHarvest(id)
    }

    fun readLocalHarvest(): LiveData<List<HarvestResult>> =
        dao.getHarvestResult()


    fun readLocalFinance(): LiveData<List<Finance>> =
        dao.getFinance()

    suspend fun insertLocalFinance(data : Finance){
        dao.insertFinance(data)
    }

    suspend fun deleteLocalFinance(id: String){
        dao.deleteFinance(id)
    }

    fun getOnBoardStatus(): LiveData<Boolean> = userPrefrences.getOnBoardKey().asLiveData()
    fun getUserName(): LiveData<String> = userPrefrences.getNameKey().asLiveData()

    suspend fun savePrefrences(onBoard : Boolean, userName: String){
        userPrefrences.savePrefrences(onBoard,userName)
    }
}