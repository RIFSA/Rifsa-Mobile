package com.example.rifsa_mobile.view.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.request.WeatherRequest
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.model.repository.local.LocalRepository
import com.example.rifsa_mobile.model.repository.remote.RemoteFirebaseRepository
import com.example.rifsa_mobile.model.repository.remote.RemoteWeatherRepository
import com.google.firebase.database.DatabaseReference

class HomeFragmentViewModel(
    private val weatherRepository: RemoteWeatherRepository,
    private val firebaseRepository: RemoteFirebaseRepository,
    private val localRepository: LocalRepository
): ViewModel() {
    fun getUserName(): LiveData<String> =
        localRepository.getUserName()
    fun getUserId(): LiveData<String> =
        localRepository.getUserIdKey()

    fun readHarvestResult(userId: String): DatabaseReference =
        firebaseRepository.queryHarvestResult(userId)

    fun readDiseaseResult(userId: String): DatabaseReference =
        firebaseRepository.readDiseaseList(userId)

    suspend fun getWeatherByLocation(request: WeatherRequest)
    : LiveData<FetchResult<WeatherDetailResponse>> =
        weatherRepository.getWeatherDataByLocation(request)

    fun getUserLocation(): LiveData<WeatherRequest> =
        localRepository.getLocationUser()

}