package com.example.rifsa_mobile.viewmodel.userpreferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifsa_mobile.model.repository.local.LocalRepository
import kotlinx.coroutines.launch

class UserPrefrencesViewModel(private val localRepository : LocalRepository): ViewModel() {

    fun getOnBoardStatus(): LiveData<Boolean> = localRepository.getOnBoardStatus()
    fun getUserName(): LiveData<String> = localRepository.getUserName()
    fun getTokenKey(): LiveData<String> = localRepository.getTokenKey()
    fun getUserId(): LiveData<String> = localRepository.getUserIdKey()

    fun saveUserPrefrences(onBoard : Boolean, userName : String, tokenKey: String, userId : String){
        viewModelScope.launch {
            localRepository.savePrefrences(onBoard,userName,tokenKey,userId)
        }
    }

}
