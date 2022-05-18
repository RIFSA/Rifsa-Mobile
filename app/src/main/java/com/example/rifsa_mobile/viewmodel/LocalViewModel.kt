package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.repository.MainRepository

class LocalViewModel(private val mainRepository: MainRepository): ViewModel() {

    fun readHarvestLocal(): LiveData<List<HarvestResult>> =
        mainRepository.readLocalHarvest()

    suspend fun insertHarvestlocal(data : HarvestResult){
        mainRepository.insertLocalHarvest(data)
    }

    suspend fun deleteHarvestLocal(id : String){
        mainRepository.deleteLocalHarvest(id)
    }


}