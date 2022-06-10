package com.example.rifsa_mobile.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.rifsa_mobile.model.entity.local.disase.Disease
import com.example.rifsa_mobile.model.entity.local.finance.Finance
import com.example.rifsa_mobile.model.entity.local.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.local.inventory.Inventory
import com.example.rifsa_mobile.model.local.databaseconfig.DatabaseConfig
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences

class LocalRepository(
    database : DatabaseConfig,
    private val userPrefrences : UserPrefrences
) {
    private val dao = database.localDao()



    fun getOnBoardStatus(): LiveData<Boolean> = userPrefrences.getOnBoardKey().asLiveData()
    fun getUserName(): LiveData<String> = userPrefrences.getNameKey().asLiveData()
    fun getUserToken(): LiveData<String> = userPrefrences.getTokenKey().asLiveData()
    suspend fun savePrefrences(onBoard : Boolean, userName: String,token : String){
        userPrefrences.savePrefrences(onBoard,userName,token)
    }


    //Local database
    suspend fun insertLocalHarvest(data : HarvestResult){
        dao.insertHarvestLocal(data)
    }

    suspend fun deleteLocalHarvest(id : String){
        dao.deleteHarvestLocal(id)
    }

    suspend fun updateHarvestLocal(uploadedStatus : String, idSort : Int){
        dao.updateHarvestLocal(uploadedStatus, idSort)
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

    fun calculateFinanceLocal(type : String): LiveData<List<Finance>>{
        return dao.calculateFinanceLocal(type)
    }

    fun readLocalInventory(): LiveData<List<Inventory>> =
        dao.getInventoryLocal()

    suspend fun insertLocalInventory(data : Inventory){
        dao.insertInventoryLocal(data)
    }

    suspend fun deleteLocalInventory(id: String){
        dao.deleteInventoryLocal(id)
    }

    fun readLocalDisease(): LiveData<List<Disease>> =
        dao.getDiseaseLocal()

    suspend fun insertLocalDisease(data : Disease){
        dao.insertDiseaseLocal(data)
    }

    suspend fun deleteLocalDisease(id: String){
        dao.deleteDiseaseLocal(id)
    }

}