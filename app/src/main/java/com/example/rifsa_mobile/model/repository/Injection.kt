package com.example.rifsa_mobile.model.repository

import android.content.Context
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences
import com.example.rifsa_mobile.model.local.prefrences.dataStore
import com.example.rifsa_mobile.model.remote.ApiConfig
import com.example.rifsa_mobile.model.remote.FirebaseService

object Injection {
    fun provideRemoteRepository(): RemoteRepository{
        return RemoteRepository(
            FirebaseService(),
            ApiConfig.setApiService()
        )
    }

    fun provideLocalRepository(context: Context): LocalRepository{
        return LocalRepository(
            UserPrefrences.getInstance(context.dataStore)
        )
    }
}