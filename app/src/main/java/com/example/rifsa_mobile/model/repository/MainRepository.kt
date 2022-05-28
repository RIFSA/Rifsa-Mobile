package com.example.rifsa_mobile.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.rifsa_mobile.model.entity.disase.Disease
import com.example.rifsa_mobile.model.entity.finance.Finance
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.inventory.Inventory
import com.example.rifsa_mobile.model.local.databaseconfig.DatabaseConfig
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences
import com.example.rifsa_mobile.model.remote.ApiService
import com.example.rifsa_mobile.model.remote.response.login.LoginBody
import com.example.rifsa_mobile.model.remote.response.login.LoginResponse
import com.example.rifsa_mobile.model.remote.response.signup.RegisterBody
import com.example.rifsa_mobile.model.remote.response.signup.RegisterResponse
import com.example.rifsa_mobile.utils.FetchResult
import kotlin.Exception

class MainRepository(
    database : DatabaseConfig,
    private val apiService: ApiService,
    private val userPrefrences: UserPrefrences
) {
    private val dao = database.localDao()


    //Remote
    suspend fun postLogin(data : LoginBody): LiveData<FetchResult<LoginResponse>> = liveData {
        emit(FetchResult.Loading)
            try {
                apiService.postLogin(data).apply {
                    emit(FetchResult.Success(this))
                }
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
    }

    suspend fun postRegister(data : RegisterBody): LiveData<FetchResult<RegisterResponse>> = liveData {
        emit(FetchResult.Loading)
        try {
            apiService.postRegister(data).apply {
                emit(FetchResult.Success(this))
            }
        }catch (e : Exception){
            emit(FetchResult.Error(e.message.toString()))
        }
    }




    //Local database
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

    fun readDisease(): LiveData<List<Disease>> =
        dao.getDiseaseLocal()

    suspend fun insertDiseaseLocal(data : Disease){
        dao.insertDiseaseLocal(data)
    }

    suspend fun deleteDiseaseLocal(id: String){
        dao.deleteDiseaseLocal(id)
    }


    fun getOnBoardStatus(): LiveData<Boolean> = userPrefrences.getOnBoardKey().asLiveData()
    fun getUserName(): LiveData<String> = userPrefrences.getNameKey().asLiveData()

    suspend fun savePrefrences(onBoard : Boolean, userName: String,token : String){
        userPrefrences.savePrefrences(onBoard,userName,token)
    }
}