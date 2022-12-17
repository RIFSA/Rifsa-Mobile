package com.example.rifsa_mobile.model.repository.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.forecast.WeatherForecastResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.request.WeatherRequest
import com.example.rifsa_mobile.model.entity.openweatherapi.request.WeatherForecastRequest
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.model.remote.weatherapi.WeatherApiService

class RemoteWeatherRepository(private var weatherApiService: WeatherApiService) {

    suspend fun getWeatherDataBySearch(location : String)
    : LiveData<FetchResult<WeatherDetailResponse>> =
        liveData{
            emit(FetchResult.Loading)
            try {
                val getWeather = weatherApiService.getWeatherDataBySearch(location)
                emit(FetchResult.Success(getWeather))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun getWeatherForecastData(request: WeatherRequest)
    : LiveData<FetchResult<WeatherForecastResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                val getForecast = weatherApiService.getWeatherForecastData(
                    location = request.location,
                    latitude = request.latitude ?: 0.0,
                    longtitude = request.longtitude ?: 0.0
                )
                emit(FetchResult.Success(getForecast))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun getWeatherDataByLocation(request : WeatherRequest)
    : LiveData<FetchResult<WeatherDetailResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                val getForecast = weatherApiService.getWeatherByLocation(
                    location = request.location,
                    latitude = request.latitude ?: 0.0,
                    longtitude = request.longtitude ?: 0.0
                )
                emit(FetchResult.Success(getForecast))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }
}