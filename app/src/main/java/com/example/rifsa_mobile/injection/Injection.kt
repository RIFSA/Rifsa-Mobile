package com.example.rifsa_mobile.injection

import android.content.Context
import com.example.rifsa_mobile.model.local.preferences.AuthenticationPreference
import com.example.rifsa_mobile.model.local.preferences.dataStore
import com.example.rifsa_mobile.model.local.room.dbconfig.DatabaseConfig
import com.example.rifsa_mobile.model.remote.firebase.FirebaseService
import com.example.rifsa_mobile.model.remote.weatherapi.WeatherApiConfig
import com.example.rifsa_mobile.model.repository.local.disease.DiseaseRepository
import com.example.rifsa_mobile.model.repository.local.financial.FinancialRepository
import com.example.rifsa_mobile.model.repository.local.harvest.HarvestRepository
import com.example.rifsa_mobile.model.repository.local.preference.PreferenceRespository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.example.rifsa_mobile.model.repository.remote.WeatherRepository

object Injection {
    fun provideFirebaseRepsitory(): FirebaseRepository {
        return FirebaseRepository(
            FirebaseService(),
        )
    }
    fun provideWeatherRepository(): WeatherRepository{
        return  WeatherRepository(WeatherApiConfig.setApiService())
    }

    fun provideLocalRepository(context: Context): PreferenceRespository {
        return PreferenceRespository(
            AuthenticationPreference.getInstance(context.dataStore)
        )
    }

    fun provideDiseaseRepository(context: Context): DiseaseRepository {
        return DiseaseRepository(DatabaseConfig.setDatabase(context))
    }

    fun provideHarvestRepository(context: Context): HarvestRepository {
        return HarvestRepository(
            DatabaseConfig.setDatabase(context),
            FirebaseService()
        )
    }

    fun provideFinancialRepository(context: Context): FinancialRepository{
        return FinancialRepository(
            DatabaseConfig.setDatabase(context),
            FirebaseService()
        )
    }


}