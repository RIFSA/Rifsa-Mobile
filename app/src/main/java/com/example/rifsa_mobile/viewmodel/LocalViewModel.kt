package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifsa_mobile.model.entity.disase.Disease
import com.example.rifsa_mobile.model.entity.finance.Finance
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.inventory.Inventory
import com.example.rifsa_mobile.model.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalViewModel(private val mainRepository: MainRepository): ViewModel() {

    fun readHarvestLocal(): LiveData<List<HarvestResult>> =
        mainRepository.readLocalHarvest()

    suspend fun insertHarvestlocal(data : HarvestResult){
        mainRepository.insertLocalHarvest(data)
    }

    suspend fun deleteHarvestLocal(id : String){
        mainRepository.deleteLocalHarvest(id)
    }

    fun readFinanceLocal(): LiveData<List<Finance>> =
        mainRepository.readLocalFinance()

    fun insertFinanceLocal(data : Finance){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.insertLocalFinance(data)
        }
    }

    suspend fun deleteFinanceLocal(id : String){
        mainRepository.deleteLocalFinance(id)
    }

    fun readInventoryLocal(): LiveData<List<Inventory>> =
        mainRepository.readLocalInventory()

    suspend fun insertInventoryLocal(data : Inventory){
        mainRepository.insertLocalInventory(data)
    }

    fun deleteInventoryLocal(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteLocalInventory(id)
        }
    }

    fun readDiseaseLocal(): LiveData<List<Disease>> =
        mainRepository.readDisease()

    fun insertDiseaseLocal(data : Disease){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.insertDiseaseLocal(data)
        }
    }

    suspend fun deleteDiseaseLocal(id: String){
        viewModelScope.launch {
            mainRepository.deleteLocalInventory(id)
        }
    }




}