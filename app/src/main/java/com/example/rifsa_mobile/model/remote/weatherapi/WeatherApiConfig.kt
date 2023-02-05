package com.example.rifsa_mobile.model.remote.weatherapi

import com.example.rifsa_mobile.BuildConfig
import com.example.rifsa_mobile.model.remote.weatherapi.utils.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiConfig {
    fun setApiService(): WeatherApiService {
        val loggingInterceptor =
            if (BuildConfig.DEBUG){
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
            }else{
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.weatherBaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(WeatherApiService::class.java)
    }


}