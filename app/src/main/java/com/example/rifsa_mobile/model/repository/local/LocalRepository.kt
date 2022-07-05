package com.example.rifsa_mobile.model.repository.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.rifsa_mobile.model.local.preferences.AuthenticationPreference

class LocalRepository(private val authenticationPreference : AuthenticationPreference) {

    fun getOnBoardStatus(): LiveData<Boolean> = authenticationPreference.getOnBoardKey().asLiveData()

    fun getUserName(): LiveData<String> = authenticationPreference.getNameKey().asLiveData()

    fun getTokenKey(): LiveData<String> = authenticationPreference.getTokenKey().asLiveData()

    fun getUserIdKey(): LiveData<String> = authenticationPreference.getUserId().asLiveData()

    suspend fun savePrefrences(onBoard : Boolean, userName: String,pass: String,token : String){
        authenticationPreference.savePreferences(onBoard,userName,pass,token)
    }


}