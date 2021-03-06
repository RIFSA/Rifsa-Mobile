package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.remote.disease.DiseasePostResponse
import com.example.rifsa_mobile.model.entity.remote.disease.restapivm.NewDiseaseResultRespon
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
import com.example.rifsa_mobile.model.repository.RemoteRepository
import com.example.rifsa_mobile.utils.FetchResult
import okhttp3.MultipartBody

class RemoteViewModel(private val remoteRepository: RemoteRepository): ViewModel() {


    suspend fun postLogin(data : LoginBody): LiveData<FetchResult<LoginResponse>> =
        remoteRepository.postLogin(data)

    suspend fun postRegister(data : RegisterBody): LiveData<FetchResult<RegisterResponse>> =
        remoteRepository.postRegister(data)


    suspend fun postHarvestRemote(data : HarvestPostBody,token: String): LiveData<FetchResult<HarvestPostResponse>> =
        remoteRepository.postHarvest(data,token)

    suspend fun updateHarvestRemote(id: Int, data: HarvestPostBody,token: String): LiveData<FetchResult<HarvestPostResponse>> =
        remoteRepository.updateHarvestRemote(id,data,token)

    suspend fun deleteHarvestRemote(id: Int,token: String): LiveData<FetchResult<HarvestPostResponse>> =
        remoteRepository.deleteHarvestRemote(id,token)

    suspend fun getHarvestRemote(token: String): LiveData<FetchResult<HarvestResultRespon>> =
        remoteRepository.getHarvestRemote(token)


    suspend fun postFinanceRemote(data: FinancePostBody,token: String): LiveData<FetchResult<FinancePostResponse>> =
        remoteRepository.postFinanceRemote(data,token)

    suspend fun getFinanceRemote(token : String): LiveData<FetchResult<FinanceResultResponse>> =
        remoteRepository.getFinanceRemote(token)

    suspend fun deleteFinanceRemote(id: Int,token: String): LiveData<FetchResult<FinancePostResponse>> =
        remoteRepository.deleteFinanceRemote(id,token)

    suspend fun updateFinanceRemote(id: Int, data: FinancePostBody,token: String): LiveData<FetchResult<FinancePostResponse>> =
        remoteRepository.updateFinanceRemote(id,data,token)



    suspend fun getInventoryRemote(token: String): LiveData<FetchResult<InventoryResultRespon>> =
        remoteRepository.getInventoryRemote(token)

    suspend fun postInventoryRemote(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String,
        token: String
    ): LiveData<FetchResult<InventoryPostResponse>> =
        remoteRepository.postInventoryRemote(name, file, jumlah, catatan,token)

    suspend fun updateInventoryRemote(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String,
        id : Int
    ): LiveData<FetchResult<InventoryPostResponse>> =
        remoteRepository.updateInventoryRemote(name, file, jumlah, catatan,id)

    suspend fun deleteInventoryRemote(id: Int,token: String): LiveData<FetchResult<InventoryPostResponse>> =
        remoteRepository.deleteInventoryRemote(id,token)



    suspend fun getDiseaseRemote(token: String): LiveData<FetchResult<NewDiseaseResultRespon>> =
        remoteRepository.getDiseaseRemote(token)

    suspend fun getDiseaseRemoteById(token: String,id: Int): LiveData<FetchResult<NewDiseaseResultRespon>> =
        remoteRepository.getDiseaseRemoteById(token, id)


    suspend fun postDiseasePrediction(
        file: MultipartBody.Part,
        latitude : Double,
        longitude: Double,
    ): LiveData<FetchResult<DiseasePostResponse>> =
        remoteRepository.postDiseasePredictionRemote(
            file,latitude, longitude
        )

    suspend fun deleteDiseaseRemote(id : Int,token: String): LiveData<FetchResult<DiseasePostResponse>> =
        remoteRepository.deleteDiseaseRemote(id,token)
}