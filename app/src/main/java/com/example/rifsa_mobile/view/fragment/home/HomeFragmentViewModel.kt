package com.example.rifsa_mobile.view.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.model.repository.local.preferenceRepository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.example.rifsa_mobile.model.repository.remote.WeatherRepository
import com.google.firebase.database.DatabaseReference

class HomeFragmentViewModel(
    private val weatherRepository: WeatherRepository,
    private val firebaseRepository: FirebaseRepository,
    private val preferenceRepository: preferenceRepository
): ViewModel() {
    fun getUserName(): LiveData<String> =
        preferenceRepository.getUserName()
    fun getUserId(): LiveData<String> =
        preferenceRepository.getUserIdKey()

    fun readHarvestResult(userId: String): DatabaseReference =
        firebaseRepository.queryHarvestResult(userId)

    fun readDiseaseResult(userId: String): DatabaseReference =
        firebaseRepository.readDiseaseList(userId)

    suspend fun getWeatherByLocation(request: UserLocation)
    : LiveData<FetchResult<WeatherDetailResponse>> =
        weatherRepository.getWeatherDataByLocation(request)

    fun getUserLocation(): LiveData<UserLocation> =
        preferenceRepository.getLocationUser()

}