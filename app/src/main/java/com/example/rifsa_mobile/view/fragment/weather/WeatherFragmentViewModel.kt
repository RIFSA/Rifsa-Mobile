package com.example.rifsa_mobile.view.fragment.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.forecast.WeatherForecastResponse
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.model.repository.remote.RemoteWeatherRepository

class WeatherFragmentViewModel(private val weatherRepository: RemoteWeatherRepository): ViewModel() {

    suspend fun getWeatherDataBySearch(location : String): LiveData<FetchResult<WeatherDetailResponse>> =
        weatherRepository.getWeatherDataBySearch(location)

    suspend fun getWeatherForecastData(location: String): LiveData<FetchResult<WeatherForecastResponse>> =
        weatherRepository.getWeatherForecastData(location)
}