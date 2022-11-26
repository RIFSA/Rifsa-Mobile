package com.example.rifsa_mobile.injection

import android.content.Context
import com.example.rifsa_mobile.model.local.preferences.AuthenticationPreference
import com.example.rifsa_mobile.model.local.preferences.dataStore
import com.example.rifsa_mobile.model.remote.firebase.FirebaseService
import com.example.rifsa_mobile.model.remote.weatherapi.WeatherApiConfig
import com.example.rifsa_mobile.model.remote.weatherapi.WeatherApiService
import com.example.rifsa_mobile.model.repository.local.LocalRepository
import com.example.rifsa_mobile.model.repository.remote.RemoteFirebaseRepository
import com.example.rifsa_mobile.model.repository.remote.RemoteWeatherRepository

object Injection {
    fun provideRemoteRepository(): RemoteFirebaseRepository {
        return RemoteFirebaseRepository(
            FirebaseService(),
        )
    }
    fun provideWeatherRepository(): RemoteWeatherRepository{
        return  RemoteWeatherRepository(WeatherApiConfig.setApiService())
    }

    fun provideLocalRepository(context: Context): LocalRepository {
        return LocalRepository(
            AuthenticationPreference.getInstance(context.dataStore)
        )
    }


}