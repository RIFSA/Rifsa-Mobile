package com.example.rifsa_mobile.viewmodel

import android.net.Uri
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
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryFirebaseEntity
import com.example.rifsa_mobile.model.repository.RemoteRepository
import com.example.rifsa_mobile.utils.FetchResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.UploadTask
import okhttp3.MultipartBody

class RemoteViewModel(private val remoteRepository: RemoteRepository): ViewModel() {


    suspend fun authLogin(email : String,password : String): LiveData<FetchResult<Task<AuthResult>>> =
        remoteRepository.authLogin(email, password)

    fun insertUpdateHarvestResult(data : HarvestFirebaseEntity, userId : String): Task<Void> =
        remoteRepository.insertUpdateHarvestResult(data, userId)

    fun readHarvestResult(userId: String): DatabaseReference =
        remoteRepository.queryHarvestResult(userId)


    fun deleteHarvestResult(date : String,dataId : String, userId : String): Task<Void> =
        remoteRepository.deleteHarvestResult(date, dataId, userId)

    fun insertUpdateFinancial(data : FinancialFirebaseEntity, userId: String): Task<Void> =
        remoteRepository.insertUpdateFinancial(data, userId)

    fun readFinancial(userId: String): DatabaseReference =
        remoteRepository.queryFinancial(userId)

    fun deleteFinancial(date : String,dataId : String, userId : String): Task<Void> =
        remoteRepository.deleteFinancial(date, dataId, userId)

    fun uploadInventoryFile(name : String, fileUri : Uri, userId: String): UploadTask =
        remoteRepository.uploadInventoryFile(name, fileUri, userId)

    fun insertInventory(data : InventoryFirebaseEntity, userId: String): Task<Void>{
        return remoteRepository.insertInventory(data, userId)
    }

    fun readInventory(userId: String): DatabaseReference{
        return remoteRepository.readInventory(userId)
    }

    fun deleteInventoryFile(name : String, userId: String): Task<Void>{
        return remoteRepository.deleteInventoryFile(name, userId)
    }

    fun deleteInventory(date : String,dataId : String,userId: String): Task<Void> {
        return remoteRepository.deleteInventory(date, dataId, userId)
    }










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