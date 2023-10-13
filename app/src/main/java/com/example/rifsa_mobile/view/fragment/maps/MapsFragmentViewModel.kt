package com.example.rifsa_mobile.view.fragment.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.entity.preferences.LastLocationPref
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.repository.local.preference.PreferenceRespository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

class MapsFragmentViewModel(
    private val firebase : FirebaseRepository,
    private val preferences: PreferenceRespository
): ViewModel(){


    fun saveDisease(data : DiseaseEntity, userId: String): Task<Void> {
        return firebase.saveDisease(data, userId)
    }

    fun readDiseaseList(userId : String): DatabaseReference {
        return firebase.readDiseaseList(userId)
    }

    fun readFarming(userId: String): DatabaseReference{
        return firebase.readFarming(userId)
    }

    //user id
    fun getUserIdKey(): LiveData<String> = preferences
        .getUserIdKey()

    //field location
    suspend fun saveFieldLocation(request: UserLocation){
        preferences.saveLocation(request)
    }
    fun getLocationReceiver(): LiveData<Boolean> = preferences
        .getLocationReceiver()

    //user last location
    fun getUserLastLocation(): LiveData<LastLocationPref> =preferences
        .getUserLastLocation()
    suspend fun saveLastLocation(locationPref: LastLocationPref) = preferences
        .saveLastLocation(locationPref)

}