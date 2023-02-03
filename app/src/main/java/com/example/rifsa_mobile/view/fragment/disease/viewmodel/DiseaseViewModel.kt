package com.example.rifsa_mobile.view.fragment.disease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.helpers.fetching.StatusRespons
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.repository.local.DiseaseRepository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.google.firebase.database.DatabaseReference

class DiseaseViewModel(
    private val diseaseRepository: DiseaseRepository,
    private val remoteRespository: FirebaseRepository
): ViewModel() {

    fun readDiseaseLocal(): LiveData<List<DiseaseEntity>>{
        return diseaseRepository.readLocalDisease()
    }

    suspend fun deleteDiseaseLocal(id : String){
       diseaseRepository.deleteLocalDisease(id)
    }
}