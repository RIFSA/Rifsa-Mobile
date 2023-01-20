package com.example.rifsa_mobile.view.fragment.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.repository.local.LocalRepository

class SettingFragmentViewModel(
    private var preferences : LocalRepository
): ViewModel() {
    fun getUserLocation(): LiveData<UserLocation> = preferences.getLocationUser()

    suspend fun saveLocation(request: UserLocation){
        preferences.saveLocation(request)
    }

    fun getLocationListener(): LiveData<Boolean> = preferences.getLocationReceiver()

    suspend fun saveLocationListener(location : Boolean){
        preferences.saveLocationListener(location)
    }

}