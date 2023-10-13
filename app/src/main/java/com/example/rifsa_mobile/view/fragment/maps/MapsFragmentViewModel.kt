package com.example.rifsa_mobile.view.fragment.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.entity.preferences.LastLocationPref
import com.example.rifsa_mobile.model.repository.local.preference.PreferenceRespository

class MapsFragmentViewModel(
    private val preferences: PreferenceRespository
): ViewModel(){

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