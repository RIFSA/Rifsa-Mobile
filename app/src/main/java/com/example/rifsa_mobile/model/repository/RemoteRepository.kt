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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import java.util.*

class RemoteRepository(
    private val apiService: ApiService,
) {

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


    suspend fun postHarvest(data : HarvestPostBody,token: String): LiveData<FetchResult<HarvestPostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(apiService.postHarvestResultRemote(data,token)))

            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
    }



    //harvest result
    suspend fun getHarvestRemote(token : String): LiveData<FetchResult<HarvestResultRespon>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(apiService.getHarvestResultRemote(token)))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
    }

    suspend fun deleteHarvestRemote(id: Int,token: String): LiveData<FetchResult<HarvestPostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.deleteHarvestResultRemote(id,token).apply {
                    emit(FetchResult.Success(this))
                }
            } catch (e: Exception) {
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun updateHarvestRemote(id: Int, data:HarvestPostBody,token: String): LiveData<FetchResult<HarvestPostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.updateHarvestResultRemote(id,data,token).apply {
                    emit(FetchResult.Success(this))
                }
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }


    suspend fun postFinanceRemote(data: FinancePostBody,token: String): LiveData<FetchResult<FinancePostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(apiService.postFinanceRemote(data,token)))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun getFinanceRemote(currentToken : String): LiveData<FetchResult<FinanceResultResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(apiService.getFinanceRemote(currentToken)))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun deleteFinanceRemote(id: Int,token: String): LiveData<FetchResult<FinancePostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.deleteFinanceRemote(id,token).apply {
                    emit(FetchResult.Success(this))
                }
            } catch (e: Exception) {
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun updateFinanceRemote(id: Int, data: FinancePostBody,token: String): LiveData<FetchResult<FinancePostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.updateFinanceRemote(id,data,token).apply {
                    emit(FetchResult.Success(this))
                }
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }


    suspend fun getInventoryRemote(token: String):LiveData<FetchResult<InventoryResultRespon>> = liveData{
        emit(FetchResult.Loading)
        try {
            emit(FetchResult.Success(
                apiService.getInventoryRemote(token)
            ))
        }catch (e : Exception){
            emit(FetchResult.Error(e.message.toString()))
        }

    }

    suspend fun postInventoryRemote(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String,
        token: String
    ): LiveData<FetchResult<InventoryPostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                apiService.postInventoryRemote(name, file, jumlah, catatan,token).apply {
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

    suspend fun deleteInventoryRemote(id : Int,token: String): LiveData<FetchResult<InventoryPostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(
                    apiService.deleteInventoryRemote(id,token)
                ))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }


    suspend fun getDiseaseRemote(token: String): LiveData<FetchResult<DiseaseResultResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(
                   apiService.getDiseaseRemote(token)
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
        token: String
    ): LiveData<FetchResult<DiseasePostResponse>> = liveData {
        emit(FetchResult.Loading)
        try {
            emit(FetchResult.Success(apiService.postDiseaseRemote(name, file, indication, description, latitude, longitude,token)))
        }catch (e : Exception){
            emit(FetchResult.Error(e.message.toString()))
        }
    }

    suspend fun deleteDiseaseRemote(id : Int,token: String): LiveData<FetchResult<DiseasePostResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                emit(FetchResult.Success(
                    apiService.deleteDiseaseRemote(id,token)
                ))
            }catch (e : Exception){
                emit(FetchResult.Error(
                    e.message.toString()
                ))
            }
        }
}