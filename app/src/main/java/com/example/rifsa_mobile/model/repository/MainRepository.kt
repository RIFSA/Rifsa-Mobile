package com.example.rifsa_mobile.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.rifsa_mobile.model.entity.local.disase.Disease
import com.example.rifsa_mobile.model.entity.local.finance.Finance
import com.example.rifsa_mobile.model.entity.local.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.local.inventory.Inventory
import com.example.rifsa_mobile.model.entity.remote.disease.DiseasePostResponse
import com.example.rifsa_mobile.model.entity.remote.disease.DiseasePredictionResponse
import com.example.rifsa_mobile.model.entity.remote.disease.DiseaseResultResponse
import com.example.rifsa_mobile.model.entity.remote.finance.FinancePostBody
import com.example.rifsa_mobile.model.entity.remote.finance.FinancePostResponse
import com.example.rifsa_mobile.model.entity.remote.finance.FinanceResultResponse
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostBody
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostResponse
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResultRespon
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryPostResponse
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryResultRespon
import com.example.rifsa_mobile.model.entity.remote.login.LoginBody
import com.example.rifsa_mobile.model.entity.remote.login.LoginResponse
import com.example.rifsa_mobile.model.entity.remote.signup.RegisterBody
import com.example.rifsa_mobile.model.entity.remote.signup.RegisterResponse
import com.example.rifsa_mobile.model.local.databaseconfig.DatabaseConfig
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences
import com.example.rifsa_mobile.model.remote.ApiService
import com.example.rifsa_mobile.utils.FetchResult
import okhttp3.MultipartBody
import java.util.*

class MainRepository(
    database : DatabaseConfig,
    private val apiService: ApiService,
    private val userPrefrences: UserPrefrences
) {
    private val dao = database.localDao()


    //Remote database
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


    suspend fun postHarvest(data : HarvestPostBody): LiveData<FetchResult<HarvestPostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(apiService.postHarvestResultRemote(data)))

            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
    }

    suspend fun getHarvest(): LiveData<FetchResult<HarvestResultRespon>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(apiService.getHarvestResultRemote()))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
    }

    suspend fun deleteHarvest(id: Int): LiveData<FetchResult<HarvestPostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.deleteHarvestResultRemote(id).apply {
                    emit(FetchResult.Success(this))
                }
            } catch (e: Exception) {
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun updateHarvest(id: Int, data:HarvestPostBody): LiveData<FetchResult<HarvestPostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.updateHarvestResultRemote(id,data).apply {
                    emit(FetchResult.Success(this))
                }
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }


    suspend fun postFinanceRemote(data: FinancePostBody): LiveData<FetchResult<FinancePostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(apiService.postFinanceRemote(data)))

            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun getFinanceRemote(): LiveData<FetchResult<FinanceResultResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(apiService.getFinanceRemote()))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun deleteFinanceRemote(id: Int): LiveData<FetchResult<FinancePostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.deleteFinanceRemote(id).apply {
                    emit(FetchResult.Success(this))
                }
            } catch (e: Exception) {
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun updateFinanceRemote(id: Int, data: FinancePostBody): LiveData<FetchResult<FinancePostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.updateFinanceRemote(id,data).apply {
                    emit(FetchResult.Success(this))
                }
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }


    suspend fun getInventoryRemote():LiveData<FetchResult<InventoryResultRespon>> = liveData{
        emit(FetchResult.Loading)
        try {
            emit(FetchResult.Success(
                apiService.getInventoryRemote()
            ))
        }catch (e : Exception){
            emit(FetchResult.Error(e.message.toString()))
        }

    }

    suspend fun postInventoryRemote(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String
    ): LiveData<FetchResult<InventoryPostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.postInventoryRemote(name, file, jumlah, catatan).apply {
                    emit(FetchResult.Success(this))
                }
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
    }

    suspend fun updateInventoryRemote(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String,
        id : Int
    ): LiveData<FetchResult<InventoryPostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.updateInventoryRemote(name, file, jumlah, catatan,id).apply {
                    emit(FetchResult.Success(this))
                }
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }


    //TODO delete Inventory Remote

    suspend fun getDiseaseRemote(): LiveData<FetchResult<DiseaseResultResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(
                   apiService.getDiseaseRemote()
                ))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun postDiseasePredictionRemote(file: MultipartBody.Part): LiveData<FetchResult<DiseasePredictionResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(
                    apiService.predictionDisease(file)
                ))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun postDiseaseRemote(
        name : String,
        file : MultipartBody.Part,
        indication : String,
        description : String,
        latitude : Double,
        longitude : Double,
    ): LiveData<FetchResult<DiseasePostResponse>> = liveData {
        emit(FetchResult.Loading)
        try {
            emit(FetchResult.Success(apiService.postDiseaseRemote(name, file, indication, description, latitude, longitude)))
        }catch (e : Exception){
            emit(FetchResult.Error(e.message.toString()))
        }
    }

    suspend fun deleteRemote(id : Int): LiveData<FetchResult<DiseasePostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(
                    apiService.deleteDiseaseRemote(id)
                ))
            }catch (e : Exception){
                emit(FetchResult.Error(
                    e.message.toString()
                ))
            }
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



    fun getOnBoardStatus(): LiveData<Boolean> = userPrefrences.getOnBoardKey().asLiveData()
    fun getUserName(): LiveData<String> = userPrefrences.getNameKey().asLiveData()
    suspend fun savePrefrences(onBoard : Boolean, userName: String,token : String){
        userPrefrences.savePrefrences(onBoard,userName,token)
    }
}