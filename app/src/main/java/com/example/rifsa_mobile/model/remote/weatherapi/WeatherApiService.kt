package com.example.rifsa_mobile.model.remote.weatherapi

import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.forecast.WeatherForecastResponse
import com.example.rifsa_mobile.model.remote.weatherapi.utils.Constant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getWeatherDataBySearch(
        @Query("q") location : String,
        @Query("units") units : String = Constant.weatherUnit,
        @Query("appid") apiKey : String = Constant.weatherApiKey,
        @Query("lang") language : String = "id"
    ):WeatherDetailResponse

    @GET("forecast")
    suspend fun getWeatherForecastData(
        @Query("q") location: String? = null,
        @Query("lat") latitude: Double,
        @Query("lon") longtitude: Double,
        @Query("units") units : String = Constant.weatherUnit,
        @Query("appid") apiKey : String = Constant.weatherApiKey,
        @Query("lang") language : String = "id"
    ): WeatherForecastResponse

    @GET("weather")
    suspend fun getWeatherByLocation(
        @Query("q") location: String? = null,
        @Query("lat") latitude: Double,
        @Query("lon") longtitude: Double,
        @Query("units") units: String = Constant.weatherUnit,
        @Query("appid") apiKey: String = Constant.weatherApiKey,
        @Query("lang") language: String = "id"
    ): WeatherDetailResponse


}