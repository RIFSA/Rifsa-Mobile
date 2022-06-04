package com.example.rifsa_mobile.model.repository

import android.content.Context
import com.example.rifsa_mobile.model.local.databaseconfig.DatabaseConfig
import com.example.rifsa_mobile.model.local.prefrences.UserPrefrences
import com.example.rifsa_mobile.model.local.prefrences.dataStore
import com.example.rifsa_mobile.model.remote.ApiConfig

object Injection {
    fun provideRepostiory(context: Context): MainRepository{
        return MainRepository(
            DatabaseConfig.getDatabase(context),
            ApiConfig.setApiService(),
            UserPrefrences.getInstance(context.dataStore)
        )
    }
}