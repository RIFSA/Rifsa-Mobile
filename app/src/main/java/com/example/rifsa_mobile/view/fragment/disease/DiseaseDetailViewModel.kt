package com.example.rifsa_mobile.view.fragment.disease

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.local.disease.DiseaseLocal
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseFirebaseEntity
import com.example.rifsa_mobile.model.repository.local.DiseaseRepository
import com.example.rifsa_mobile.model.repository.remote.RemoteFirebaseRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.UploadTask

class DiseaseDetailViewModel(
    private val diseaseRepository: DiseaseRepository,
    private val remoteRespository: RemoteFirebaseRepository
): ViewModel() {
    fun getDiseaseInformation(id : String): DatabaseReference {
        return remoteRespository.getDiseaseInformation(id)
    }

    fun getDiseaseInformationMisc(id : String,parent : String): DatabaseReference {
        return remoteRespository.getDiseaseInformationMisc(id, parent)
    }

    fun uploadDiseaseImage(name : String, fileUri : Uri, userId: String): UploadTask {
        return remoteRespository.uploadDiseaseImage(name, fileUri, userId)
    }

    fun saveDisease(data : DiseaseFirebaseEntity, userId: String): Task<Void> {
        return remoteRespository.saveDisease(data, userId)
    }

    fun deleteDisease(date : String,dataId : String,userId: String): Task<Void> {
        return remoteRespository.deleteDisease(date, dataId, userId)
    }

    fun deleteDiseaseImage(name : String, userId: String): Task<Void> {
        return remoteRespository.deleteDiseaseImage(name, userId)
    }

    suspend fun insertDiseaseLocal(data : DiseaseLocal){
        diseaseRepository.insertLocalDisease(data)
    }


}