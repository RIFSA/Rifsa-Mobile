package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifsa_mobile.model.repository.MainRepository
import kotlinx.coroutines.launch

class UserPrefrencesViewModel
    (private val repository : MainRepository): ViewModel() {

    fun getOnBoardStatus(): LiveData<Boolean> = repository.getOnBoardStatus()
    fun getUserName(): LiveData<String> = repository.getUserName()

    fun saveUserPrefrences(onBoard : Boolean,userName : String){
        viewModelScope.launch {
            repository.savePrefrences(onBoard, userName)
        }
    }

}
