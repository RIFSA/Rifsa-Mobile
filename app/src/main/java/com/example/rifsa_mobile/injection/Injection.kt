package com.example.rifsa_mobile.injection

import android.content.Context
import com.example.rifsa_mobile.model.local.preferences.AuthenticationPreference
import com.example.rifsa_mobile.model.local.preferences.dataStore
import com.example.rifsa_mobile.model.remote.firebase.FirebaseService
import com.example.rifsa_mobile.model.repository.local.LocalRepository
import com.example.rifsa_mobile.model.repository.remote.RemoteRepository

object Injection {
    fun provideRemoteRepository(): RemoteRepository {
        return RemoteRepository(
            FirebaseService(),
        )
    }

    fun provideLocalRepository(context: Context): LocalRepository {
        return LocalRepository(
            AuthenticationPreference.getInstance(context.dataStore)
        )
    }
}