package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifsa_mobile.model.entity.local.disase.Disease
import com.example.rifsa_mobile.model.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalViewModel(private val localRepository: LocalRepository): ViewModel() {

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