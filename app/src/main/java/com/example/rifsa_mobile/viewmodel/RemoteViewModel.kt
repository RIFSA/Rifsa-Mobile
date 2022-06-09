package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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
import com.example.rifsa_mobile.model.repository.RemoteRepository
import com.example.rifsa_mobile.utils.FetchResult
import okhttp3.MultipartBody

class RemoteViewModel(private val remoteRepository: RemoteRepository): ViewModel() {

    suspend fun postLogin(data : LoginBody): LiveData<FetchResult<LoginResponse>> =
        remoteRepository.postLogin(data)

    suspend fun postRegister(data : RegisterBody): LiveData<FetchResult<RegisterResponse>> =
        remoteRepository.postRegister(data)


    suspend fun postHarvestRemote(data : HarvestPostBody): LiveData<FetchResult<HarvestPostResponse>> =
        remoteRepository.postHarvest(data)

    suspend fun updateHarvestRemote(id: Int, data: HarvestPostBody): LiveData<FetchResult<HarvestPostResponse>> =
        remoteRepository.updateHarvestRemote(id,data)

    suspend fun deleteHarvestRemote(id: Int): LiveData<FetchResult<HarvestPostResponse>> =
        remoteRepository.deleteHarvestRemote(id)

    suspend fun getHarvestRemote(): LiveData<FetchResult<HarvestResultRespon>> =
        remoteRepository.getHarvestRemote()


    suspend fun postFinanceRemote(data: FinancePostBody): LiveData<FetchResult<FinancePostResponse>> =
        remoteRepository.postFinanceRemote(data)

    suspend fun getFinanceRemote(): LiveData<FetchResult<FinanceResultResponse>> =
        remoteRepository.getFinanceRemote()

    suspend fun deleteFinanceRemote(id: Int): LiveData<FetchResult<FinancePostResponse>> =
        remoteRepository.deleteFinanceRemote(id)

    suspend fun updateFinanceRemote(id: Int, data: FinancePostBody): LiveData<FetchResult<FinancePostResponse>> =
        remoteRepository.updateFinanceRemote(id,data)



    suspend fun getInventoryRemote(): LiveData<FetchResult<InventoryResultRespon>> =
        remoteRepository.getInventoryRemote()

    suspend fun postInventoryRemote(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String
    ): LiveData<FetchResult<InventoryPostResponse>> =
        remoteRepository.postInventoryRemote(name, file, jumlah, catatan)

    suspend fun updateInventoryRemote(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String,
        id : Int
    ): LiveData<FetchResult<InventoryPostResponse>> =
        remoteRepository.updateInventoryRemote(name, file, jumlah, catatan,id)

    suspend fun deleteInventoryRemote(id: Int): LiveData<FetchResult<InventoryPostResponse>> =
        remoteRepository.deleteInventoryRemote(id)



    suspend fun getDiseaseRemote(): LiveData<FetchResult<DiseaseResultResponse>> =
        remoteRepository.getDiseaseRemote()

    suspend fun postDiseasePrediction(file: MultipartBody.Part): LiveData<FetchResult<DiseasePredictionResponse>> =
        remoteRepository.postDiseasePredictionRemote(file)

    suspend fun postDiseaseRemote(
        name : String,
        file : MultipartBody.Part,
        indication : String,
        description : String,
        latitude : Double,
        longitude : Double,
    ): LiveData<FetchResult<DiseasePostResponse>> =
        remoteRepository.postDiseaseRemote(name, file, indication, description, latitude, longitude)

    suspend fun deleteDiseaseRemote(id : Int): LiveData<FetchResult<DiseasePostResponse>> =
        remoteRepository.deleteDiseaseRemote(id)
}