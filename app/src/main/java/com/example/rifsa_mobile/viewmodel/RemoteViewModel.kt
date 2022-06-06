package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.local.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostBody
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResultRespon
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryRespon
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

    suspend fun postHarvest(data : HarvestPostBody): LiveData<FetchResult<HarvestResultRespon>> =
        mainRepository.postHarvest(data)

    suspend fun updateHarvest(id: Int, data: HarvestPostBody): LiveData<FetchResult<HarvestResultRespon>> =
        mainRepository.updateHarvest(id,data)

    suspend fun deleteHarvest(id: Int): LiveData<FetchResult<HarvestResultRespon>> =
        mainRepository.deleteHarvest(id)

    suspend fun getHarvest(): LiveData<FetchResult<HarvestResultRespon>> =
        mainRepository.getHarvest()

    suspend fun postInventory(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String
    ): LiveData<FetchResult<InventoryRespon>> =
        mainRepository.postInventoryRemote(name, file, jumlah, catatan)
}