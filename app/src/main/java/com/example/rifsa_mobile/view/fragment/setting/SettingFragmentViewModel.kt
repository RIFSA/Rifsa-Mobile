package com.example.rifsa_mobile.view.fragment.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.repository.local.LocalRepository

class SettingFragmentViewModel(
    private var preferences : LocalRepository
): ViewModel() {
    fun getUserLocation(): LiveData<List<Float>> = preferences.getLocationUser()

    suspend fun saveLocation(lattidue : Float, longtitude : Float){
        preferences.saveLocation(longtitude,lattidue)
    }

    fun getLocationListener(): LiveData<Boolean> = preferences.getLocationReceiver()

    suspend fun saveLocationListener(location : Boolean){
        preferences.saveLocationListener(location)
    }

}