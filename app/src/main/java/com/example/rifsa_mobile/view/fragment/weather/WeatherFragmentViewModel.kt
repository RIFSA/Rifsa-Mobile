package com.example.rifsa_mobile.view.fragment.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.forecast.WeatherForecastResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.model.repository.local.preference.PreferenceRespository
import com.example.rifsa_mobile.model.repository.remote.WeatherRepository

class WeatherFragmentViewModel(
    private val weatherRepository: WeatherRepository,
    private val PreferenceRespository: PreferenceRespository
    ): ViewModel() {

    fun getUserLocation():LiveData<UserLocation> =
        PreferenceRespository.getLocationUser()

    suspend fun getWeatherDataBySearch(location : String)
    : LiveData<FetchResult<WeatherDetailResponse>> =
        weatherRepository.getWeatherDataBySearch(location)

    suspend fun getWeatherForecastData(request: UserLocation)
    : LiveData<FetchResult<WeatherForecastResponse>> =
        weatherRepository.getWeatherForecastData(request)

    suspend fun getWeatherDataByLocation(request : UserLocation)
    : LiveData<FetchResult<WeatherDetailResponse>> =
        weatherRepository.getWeatherDataByLocation(request)
}