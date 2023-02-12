package com.example.rifsa_mobile.viewmodel.userpreferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifsa_mobile.model.repository.local.preference.PreferenceRespository
import kotlinx.coroutines.launch

class UserPrefrencesViewModel(
    private val PreferenceRespository : PreferenceRespository
): ViewModel() {

    fun getOnBoardStatus(): LiveData<Boolean> = PreferenceRespository.getOnBoardStatus()
    fun getUserName(): LiveData<String> = PreferenceRespository.getUserName()
    fun getTokenKey(): LiveData<String> = PreferenceRespository.getTokenKey()
    fun getUserId(): LiveData<String> = PreferenceRespository.getUserIdKey()

    fun saveUserPrefrences(onBoard : Boolean, userName : String, tokenKey: String, userId : String){
        viewModelScope.launch {
            PreferenceRespository.savePrefrences(onBoard,userName,tokenKey,userId)
        }
    }

}
