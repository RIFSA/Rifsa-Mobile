package com.example.rifsa_mobile.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.rifsa_mobile.model.entity.local.disase.Disease
import com.example.rifsa_mobile.model.entity.local.finance.Finance
import com.example.rifsa_mobile.model.entity.local.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.local.inventory.Inventory
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostBody
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResultRespon
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryRespon
import com.example.rifsa_mobile.model.entity.remote.login.LoginBody
import com.example.rifsa_mobile.model.entity.remote.login.LoginResponse
import com.example.rifsa_mobile.model.entity.remote.signup.RegisterBody
import com.example.rifsa_mobile.model.entity.remote.signup.RegisterResponse
import com.example.rifsa_mobile.model.local.databaseconfig.DatabaseConfig
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences
import com.example.rifsa_mobile.model.remote.ApiService
import com.example.rifsa_mobile.utils.FetchResult
import okhttp3.MultipartBody

class MainRepository(
    database : DatabaseConfig,
    private val apiService: ApiService,
    private val userPrefrences: UserPrefrences
) {
    private val dao = database.localDao()


    //Remote
    suspend fun postLogin(data : LoginBody): LiveData<FetchResult<LoginResponse>> =
        liveData {
            emit(FetchResult.Loading)
                try {
                    apiService.postLogin(data).apply {
                        emit(FetchResult.Success(this))
                    }
                }catch (e : Exception){
                    emit(FetchResult.Error(e.message.toString()))
                }
    }

    suspend fun postRegister(data : RegisterBody): LiveData<FetchResult<RegisterResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.postRegister(data).apply {
                    emit(FetchResult.Success(this))
                }
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
    }


    suspend fun postHarvest(data : HarvestPostBody): LiveData<FetchResult<HarvestResultRespon>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.postHarvestResult(data).apply {
                    emit(FetchResult.Success(this))
                }
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
    }

    suspend fun getHarvest(): LiveData<FetchResult<HarvestResultRespon>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(apiService.getHarvestResult()))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
    }

    suspend fun updateHarvestLocal(uploadedStatus : Boolean, idSort : Int){
        dao.updateHarvestLocal(uploadedStatus, idSort)
    }

    suspend fun postInventoryRemote(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String
    ): LiveData<FetchResult<InventoryRespon>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.postInventory(name, file, jumlah, catatan).apply {
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