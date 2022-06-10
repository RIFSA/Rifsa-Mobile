package com.example.rifsa_mobile.model.repository

import android.content.Context
import com.example.rifsa_mobile.model.local.databaseconfig.DatabaseConfig
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences
import com.example.rifsa_mobile.model.local.prefrences.dataStore
import com.example.rifsa_mobile.model.remote.ApiConfig

object Injection {
    fun provideRemoteRepository(context: Context): RemoteRepository{
        return RemoteRepository(
            ApiConfig.setApiService()
        )
    }

    fun provideLocalRepository(context: Context): LocalRepository{
        return LocalRepository(
            DatabaseConfig.getDatabase(context),
            UserPrefrences.getInstance(context.dataStore)
        )
    }
}