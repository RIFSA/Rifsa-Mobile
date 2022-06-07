package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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

    suspend fun postHarvest(data : HarvestPostBody): LiveData<FetchResult<HarvestPostResponse>> =
        mainRepository.postHarvest(data)

    suspend fun updateHarvest(id: Int, data: HarvestPostBody): LiveData<FetchResult<HarvestPostResponse>> =
        mainRepository.updateHarvest(id,data)

    suspend fun deleteHarvest(id: Int): LiveData<FetchResult<HarvestPostResponse>> =
        mainRepository.deleteHarvest(id)

    suspend fun getHarvest(): LiveData<FetchResult<HarvestResultRespon>> =
        mainRepository.getHarvest()

    suspend fun postFinance(data: FinancePostBody): LiveData<FetchResult<FinancePostResponse>> =
        mainRepository.postFinanceRemote(data)

    suspend fun getFinance(): LiveData<FetchResult<FinanceResultResponse>> =
        mainRepository.getFinanceRemote()

    suspend fun deleteFinance(id: Int): LiveData<FetchResult<FinancePostResponse>> =
        mainRepository.deleteFinanceRemote(id)

    suspend fun updateFinance(id: Int, data: FinancePostBody): LiveData<FetchResult<FinancePostResponse>> =
        mainRepository.updateFinanceRemote(id,data)


    suspend fun getInventory(): LiveData<FetchResult<InventoryResultRespon>> =
        mainRepository.getInventoryRemote()

    suspend fun postInventory(
        name : String,
        file : MultipartBody.Part,
        jumlah : Int,
        catatan : String
    ): LiveData<FetchResult<InventoryPostResponse>> =
        mainRepository.postInventoryRemote(name, file, jumlah, catatan)
}