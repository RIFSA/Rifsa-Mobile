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
import com.example.rifsa_mobile.model.repository.MainRepository
import com.example.rifsa_mobile.utils.FetchResult
import okhttp3.MultipartBody

class RemoteViewModel(private val mainRepository: MainRepository): ViewModel() {

    suspend fun postLogin(data : LoginBody): LiveData<FetchResult<LoginResponse>> =
        mainRepository.postLogin(data)

    suspend fun postRegister(data : RegisterBody): LiveData<FetchResult<RegisterResponse>> =
        mainRepository.postRegister(data)


    suspend fun postHarvestRemote(data : HarvestPostBody): LiveData<FetchResult<HarvestPostResponse>> =
        mainRepository.postHarvest(data)

    suspend fun updateHarvestRemote(id: Int, data: HarvestPostBody): LiveData<FetchResult<HarvestPostResponse>> =
        mainRepository.updateHarvestRemote(id,data)

    suspend fun deleteHarvestRemote(id: Int): LiveData<FetchResult<HarvestPostResponse>> =
        mainRepository.deleteHarvestRemote(id)

    suspend fun getHarvestRemote(): LiveData<FetchResult<HarvestResultRespon>> =
        mainRepository.getHarvestRemote()


    suspend fun postFinanceRemote(data: FinancePostBody): LiveData<FetchResult<FinancePostResponse>> =
        mainRepository.postFinanceRemote(data)

    suspend fun getFinanceRemote(): LiveData<FetchResult<FinanceResultResponse>> =
        mainRepository.getFinanceRemote()

    suspend fun deleteFinanceRemote(id: Int): LiveData<FetchResult<FinancePostResponse>> =
        mainRepository.deleteFinanceRemote(id)

    suspend fun updateFinanceRemote(id: Int, data: FinancePostBody): LiveData<FetchResult<FinancePostResponse>> =
        mainRepository.updateFinanceRemote(id,data)



    suspend fun getInventoryRemote(): LiveData<FetchResult<InventoryResultRespon>> =
        mainRepository.getInventoryRemote()

    suspend fun postInventoryRemote(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String
    ): LiveData<FetchResult<InventoryPostResponse>> =
        mainRepository.postInventoryRemote(name, file, jumlah, catatan)

    suspend fun updateInventoryRemote(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String,
        id : Int
    ): LiveData<FetchResult<InventoryPostResponse>> =
        mainRepository.updateInventoryRemote(name, file, jumlah, catatan,id)

    suspend fun deleteInventoryRemote(id: Int): LiveData<FetchResult<InventoryPostResponse>> =
        mainRepository.deleteInventoryRemote(id)



    suspend fun getDiseaseRemote(): LiveData<FetchResult<DiseaseResultResponse>> =
        mainRepository.getDiseaseRemote()

    suspend fun postDiseasePrediction(file: MultipartBody.Part): LiveData<FetchResult<DiseasePredictionResponse>> =
        mainRepository.postDiseasePredictionRemote(file)

    suspend fun postDiseaseRemote(
        name : String,
        file : MultipartBody.Part,
        indication : String,
        description : String,
        latitude : Double,
        longitude : Double,
    ): LiveData<FetchResult<DiseasePostResponse>> =
        mainRepository.postDiseaseRemote(name, file, indication, description, latitude, longitude)

    suspend fun deleteDiseaseRemote(id : Int): LiveData<FetchResult<DiseasePostResponse>> =
        mainRepository.deleteDiseaseRemote(id)
}