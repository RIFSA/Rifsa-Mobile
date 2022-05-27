package com.example.rifsa_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.remote.response.login.LoginBody
import com.example.rifsa_mobile.model.remote.response.login.LoginResponse
import com.example.rifsa_mobile.model.repository.MainRepository
import com.example.rifsa_mobile.utils.FetchResult

class RemoteViewModel(private val mainRepository: MainRepository): ViewModel() {

    suspend fun postLogin(data : LoginBody): LiveData<FetchResult<LoginResponse>> =
        mainRepository.postLogin(data)
}