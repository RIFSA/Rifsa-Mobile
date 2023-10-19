package com.example.rifsa_mobile.view.fragment.disease.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.repository.local.disease.DiseaseRepository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository

class DiseaseViewModel(
    private val diseaseRepository: DiseaseRepository,
    private val remoteRespository: FirebaseRepository
): ViewModel() {

    fun readDiseaseLocal(): LiveData<List<DiseaseEntity>>{
        return diseaseRepository.readLocalDisease()
    }

    //sort data
    fun readDiseaseSortNameAsc(): LiveData<PagedList<DiseaseEntity>> =
        diseaseRepository.readDiseaseSortNameDesc()

    fun readDiseaseSortNameDesc(): LiveData<PagedList<DiseaseEntity>> =
        diseaseRepository.readDiseaseSortNameDesc()

    fun readDiseaseSortDateAsc(): LiveData<PagedList<DiseaseEntity>> =
        diseaseRepository.readDiseaseSortDateAsc()

    fun readDiseaseSortDateDesc(): LiveData<PagedList<DiseaseEntity>> =
        diseaseRepository.readDiseaseSortDateDesc()


}