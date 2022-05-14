package com.example.rifsa_mobile.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.rifsa_mobile.model.local.UserPrefrences
import javax.net.ssl.SSLEngineResult

class MainRepository(private val userPrefrences: UserPrefrences) {
    fun getOnBoardStatus(): LiveData<Boolean> = userPrefrences.getOnBoardKey().asLiveData()

    fun getUserName(): LiveData<String> = userPrefrences.getNameKey().asLiveData()

    suspend fun savePrefrences(onBoard : Boolean, userName: String){
        userPrefrences.savePrefrences(onBoard,userName)
    }
}