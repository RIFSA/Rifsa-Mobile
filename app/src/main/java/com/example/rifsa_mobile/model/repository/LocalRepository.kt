package com.example.rifsa_mobile.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences

class LocalRepository(private val userPrefrences : UserPrefrences) {
    fun getOnBoardStatus(): LiveData<Boolean> = userPrefrences.getOnBoardKey().asLiveData()
    fun getUserName(): LiveData<String> = userPrefrences.getNameKey().asLiveData()
    fun getTokenKey(): LiveData<String> = userPrefrences.getTokenKey().asLiveData()
    fun getUserIdKey(): LiveData<String> = userPrefrences.getUserId().asLiveData()
    suspend fun savePrefrences(onBoard : Boolean, userName: String,pass: String,token : String){
        userPrefrences.savePrefrences(onBoard,userName,pass,token)
    }


}