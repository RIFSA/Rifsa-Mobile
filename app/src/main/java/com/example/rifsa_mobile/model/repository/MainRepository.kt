package com.example.rifsa_mobile.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.rifsa_mobile.model.entity.finance.Finance
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.inventory.Inventory
import com.example.rifsa_mobile.model.local.databaseconfig.DatabaseConfig
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences

class MainRepository(
    database : DatabaseConfig,
    private val userPrefrences: UserPrefrences
) {

    private val dao = database.localDao()

    suspend fun insertLocalHarvest(data : HarvestResult){
        dao.insertHarvestLocal(data)
    }

    suspend fun deleteLocalHarvest(id : String){
        dao.deleteHarvestLocal(id)
    }

    fun readLocalHarvest(): LiveData<List<HarvestResult>> =
        dao.getHarvestLocal()


    fun readLocalFinance(): LiveData<List<Finance>> =
        dao.getFinanceLocal()

    suspend fun insertLocalFinance(data : Finance){
        dao.insertFinanceLocal(data)
    }

    suspend fun deleteLocalFinance(id: String){
        dao.deleteFinanceLocal(id)
    }

    fun readLocalInventory(): LiveData<List<Inventory>> =
        dao.getInventoryLocal()

    suspend fun insertLocalInventory(data : Inventory){
        dao.insertInventoryLocal(data)
    }

    suspend fun deleteLocalInventory(id: String){
        dao.deleteInventoryLocal(id)
    }


    fun getOnBoardStatus(): LiveData<Boolean> = userPrefrences.getOnBoardKey().asLiveData()
    fun getUserName(): LiveData<String> = userPrefrences.getNameKey().asLiveData()

    suspend fun savePrefrences(onBoard : Boolean, userName: String){
        userPrefrences.savePrefrences(onBoard,userName)
    }
}