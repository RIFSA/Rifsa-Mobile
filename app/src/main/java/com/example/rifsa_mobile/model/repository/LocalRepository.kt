package com.example.rifsa_mobile.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.rifsa_mobile.model.entity.local.disase.Disease
import com.example.rifsa_mobile.model.local.databaseconfig.DatabaseConfig
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences

class LocalRepository(
    database : DatabaseConfig,
    private val userPrefrences : UserPrefrences
) {
    private val dao = database.localDao()

    fun getOnBoardStatus(): LiveData<Boolean> = userPrefrences.getOnBoardKey().asLiveData()
    fun getUserName(): LiveData<String> = userPrefrences.getNameKey().asLiveData()
    fun getPassword(): LiveData<String> = userPrefrences.getPassKey().asLiveData()
    fun getUserToken(): LiveData<String> = userPrefrences.getTokenKey().asLiveData()
    suspend fun savePrefrences(onBoard : Boolean, userName: String,pass: String,token : String){
        userPrefrences.savePrefrences(onBoard,userName,pass,token)
    }

    suspend fun insertLocalDisease(data : Disease){
        dao.insertDiseaseLocal(data)
    }

    suspend fun deleteLocalDisease(id: String){
        dao.deleteDiseaseLocal(id)
    }

}