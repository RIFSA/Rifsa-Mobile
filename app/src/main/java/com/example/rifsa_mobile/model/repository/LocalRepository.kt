package com.example.rifsa_mobile.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences

class LocalRepository(
    private val userPrefrences : UserPrefrences
) {
    fun getOnBoardStatus(): LiveData<Boolean> = userPrefrences.getOnBoardKey().asLiveData()
    fun getUserName(): LiveData<String> = userPrefrences.getNameKey().asLiveData()
    fun getPassword(): LiveData<String> = userPrefrences.getPassKey().asLiveData()
    fun getUserToken(): LiveData<String> = userPrefrences.getTokenKey().asLiveData()
    suspend fun savePrefrences(onBoard : Boolean, userName: String,pass: String,token : String){
        userPrefrences.savePrefrences(onBoard,userName,pass,token)
    }


}