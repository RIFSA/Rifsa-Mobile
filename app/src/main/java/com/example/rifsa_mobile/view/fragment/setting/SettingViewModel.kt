package com.example.rifsa_mobile.view.fragment.setting

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.repository.local.disease.DiseaseRepository
import com.example.rifsa_mobile.model.repository.local.preference.PreferenceRespository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

class SettingViewModel(
    private val remoteRepository : FirebaseRepository,
    private val localRepository: DiseaseRepository,
    private var preferences : PreferenceRespository
): ViewModel() {
    fun getDiseaseNotUploaded(): LiveData<List<DiseaseEntity>> =
        localRepository.readLocalDisease()

    fun insertDiseaseImage(name : String, fileUri : Uri, userId: String): UploadTask {
        return remoteRepository.uploadDiseaseImage(name, fileUri, userId)
    }

    fun insertDiseaseRemote(data : DiseaseEntity, userId: String): Task<Void> =
        remoteRepository.saveDisease(data, userId)

    fun getFirebaseUserId(): LiveData<String> =
        preferences.getUserIdKey()

    fun getLocationListener(): LiveData<Boolean> =
        preferences.getLocationReceiver()

    suspend fun saveLocation(request: UserLocation){
        preferences.saveLocation(request)
    }

    suspend fun saveLocationListener(location : Boolean){
        preferences.saveLocationListener(location)
    }

    suspend fun updateDiseaseUpload(imageUri : Uri,idDisease : String){
        localRepository.updateDiseaseUpload(imageUri, idDisease)
    }

}