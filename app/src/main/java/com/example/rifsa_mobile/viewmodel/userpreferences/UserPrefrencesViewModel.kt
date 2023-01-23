package com.example.rifsa_mobile.viewmodel.userpreferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifsa_mobile.model.repository.local.preferenceRepository
import kotlinx.coroutines.launch

class UserPrefrencesViewModel(
    private val preferenceRepository : preferenceRepository
): ViewModel() {

    fun getOnBoardStatus(): LiveData<Boolean> = preferenceRepository.getOnBoardStatus()
    fun getUserName(): LiveData<String> = preferenceRepository.getUserName()
    fun getTokenKey(): LiveData<String> = preferenceRepository.getTokenKey()
    fun getUserId(): LiveData<String> = preferenceRepository.getUserIdKey()

    fun saveUserPrefrences(onBoard : Boolean, userName : String, tokenKey: String, userId : String){
        viewModelScope.launch {
            preferenceRepository.savePrefrences(onBoard,userName,tokenKey,userId)
        }
    }

}
