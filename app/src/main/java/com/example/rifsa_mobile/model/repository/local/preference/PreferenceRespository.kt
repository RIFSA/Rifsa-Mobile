package com.example.rifsa_mobile.model.repository.local.preference

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.local.preferences.AuthenticationPreference

class PreferenceRespository(
    private val preferences : AuthenticationPreference
) {

    fun getOnBoardStatus(): LiveData<Boolean> = preferences.getOnBoardKey().asLiveData()

    fun getUserName(): LiveData<String> = preferences.getNameKey().asLiveData()

    fun getTokenKey(): LiveData<String> = preferences.getTokenKey().asLiveData()

    fun getUserIdKey(): LiveData<String> = preferences.getUserId().asLiveData()

    fun getLocationUser(): LiveData<UserLocation> = preferences.getUserLocation().asLiveData()

    fun getLocationReceiver(): LiveData<Boolean> = preferences.getLocationListener().asLiveData()

    suspend fun savePrefrences(onBoard : Boolean, userName: String,pass: String,token : String){
        preferences.savePreferences(onBoard,userName,pass,token)
    }

    suspend fun saveLocation(request: UserLocation){
        preferences.saveLocation(
            request.location ?: "",
            request.latitude ?: 0.0,
            request.longtitude ?: 0.0
        )
    }

    suspend fun saveLocationListener(location : Boolean){
        preferences.setGetLocation(location)
    }


}