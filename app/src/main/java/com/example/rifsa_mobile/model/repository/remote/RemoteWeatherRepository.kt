package com.example.rifsa_mobile.model.repository.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.forecast.WeatherForecastResponse
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.model.remote.weatherapi.WeatherApiService
import java.util.concurrent.Flow

class RemoteWeatherRepository(private var weatherApiService: WeatherApiService) {

    suspend fun getWeatherDataBySearch(location : String): LiveData<FetchResult<WeatherDetailResponse>> =
        liveData{
            emit(FetchResult.Loading)
            try {
                val getWeather = weatherApiService.getWeatherDataBySearch(location)
                emit(FetchResult.Success(getWeather))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }

    suspend fun getWeatherForecastData(location: String): LiveData<FetchResult<WeatherForecastResponse>> =
        liveData {
            emit(FetchResult.Loading)
            try {
                val getForecast = weatherApiService.getWeatherForecastData(location)
                emit(FetchResult.Success(getForecast))
            }catch (e : Exception){
                emit(FetchResult.Error(e.message.toString()))
            }
        }
}