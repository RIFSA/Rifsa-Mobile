package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifsa_mobile.model.repository.LocalRepository
import kotlinx.coroutines.launch

class UserPrefrencesViewModel(private val localRepository : LocalRepository): ViewModel() {

    fun getOnBoardStatus(): LiveData<Boolean> = localRepository.getOnBoardStatus()
    fun getUserName(): LiveData<String> = localRepository.getUserName()
    fun getUserToken(): LiveData<String> = localRepository.getUserToken()

    fun saveUserPrefrences(onBoard : Boolean,userName : String,token : String){
        viewModelScope.launch {
            localRepository.savePrefrences(onBoard, userName,token)
        }
    }

}
