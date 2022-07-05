package com.example.rifsa_mobile.model.repository

import android.content.Context
import com.example.rifsa_mobile.model.local.preferences.AuthenticationPreference
import com.example.rifsa_mobile.model.local.preferences.dataStore
import com.example.rifsa_mobile.model.remote.FirebaseService

object Injection {
    fun provideRemoteRepository(): RemoteRepository{
        return RemoteRepository(
            FirebaseService(),
        )
    }

    fun provideLocalRepository(context: Context): LocalRepository{
        return LocalRepository(
            AuthenticationPreference.getInstance(context.dataStore)
        )
    }
}