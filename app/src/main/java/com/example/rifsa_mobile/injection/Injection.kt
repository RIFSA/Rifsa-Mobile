package com.example.rifsa_mobile.injection

import android.content.Context
import com.example.rifsa_mobile.model.local.preferences.AuthenticationPreference
import com.example.rifsa_mobile.model.local.preferences.dataStore
import com.example.rifsa_mobile.model.local.room.dbconfig.DbConfig
import com.example.rifsa_mobile.model.remote.firebase.FirebaseService
import com.example.rifsa_mobile.model.remote.weatherapi.WeatherApiConfig
import com.example.rifsa_mobile.model.repository.local.DiseaseRepository
import com.example.rifsa_mobile.model.repository.local.preferenceRepository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.example.rifsa_mobile.model.repository.remote.WeatherRepository

object Injection {
    fun provideRemoteRepository(): FirebaseRepository {
        return FirebaseRepository(
            FirebaseService(),
        )
    }
    fun provideWeatherRepository(): WeatherRepository{
        return  WeatherRepository(WeatherApiConfig.setApiService())
    }

    fun provideLocalRepository(context: Context): preferenceRepository {
        return preferenceRepository(
            AuthenticationPreference.getInstance(context.dataStore)
        )
    }

    fun provideDiseaseRepository(context: Context): DiseaseRepository{
        return DiseaseRepository(DbConfig.setDatabase(context))
    }


}