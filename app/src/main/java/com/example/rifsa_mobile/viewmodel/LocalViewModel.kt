package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifsa_mobile.model.entity.local.disase.Disease
import com.example.rifsa_mobile.model.entity.local.finance.Finance
import com.example.rifsa_mobile.model.entity.local.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.local.inventory.Inventory
import com.example.rifsa_mobile.model.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalViewModel(private val localRepository: LocalRepository): ViewModel() {

    fun readHarvestLocal(): LiveData<List<HarvestResult>> =
        localRepository.readLocalHarvest()


    fun calculateFinanceLocal(type : String): LiveData<List<Finance>>{
        return localRepository.calculateFinanceLocal(type)
    }


    fun readInventoryLocal(): LiveData<List<Inventory>> =
        localRepository.readLocalInventory()



    fun readDiseaseLocal(): LiveData<List<Disease>> =
        localRepository.readLocalDisease()

    fun insertDiseaseLocal(data : Disease){
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.insertLocalDisease(data)
        }
    }

    fun deleteDiseaseLocal(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.deleteLocalDisease(id)
        }
    }




}