package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifsa_mobile.model.entity.local.disase.Disease
import com.example.rifsa_mobile.model.entity.local.finance.Finance
import com.example.rifsa_mobile.model.entity.local.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.local.inventory.Inventory
import com.example.rifsa_mobile.model.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalViewModel(private val mainRepository: MainRepository): ViewModel() {

    fun readHarvestLocal(): LiveData<List<HarvestResult>> =
        mainRepository.readLocalHarvest()

    fun insertHarvestlocal(data : HarvestResult){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.insertLocalHarvest(data)
        }
    }

    suspend fun deleteHarvestLocal(id : String){
        mainRepository.deleteLocalHarvest(id)
    }

    suspend fun updateHarvestLocal(uploadedStatus : Boolean,idSort : Int){
        mainRepository.updateHarvestLocal(uploadedStatus, idSort)
    }

    fun readFinanceLocal(): LiveData<List<Finance>> =
        mainRepository.readLocalFinance()

    fun insertFinanceLocal(data : Finance){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.insertLocalFinance(data)
        }
    }
     fun deleteFinanceLocal(id : String){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteLocalFinance(id)
        }
    }

    fun calculateFinanceLocal(type : String): LiveData<List<Finance>>{
        return mainRepository.calculateFinanceLocal(type)
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

    fun deleteDiseaseLocal(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.deleteDiseaseLocal(id)
        }
    }




}