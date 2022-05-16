package com.example.rifsa_mobile.model.repository

import android.content.Context
import com.example.rifsa_mobile.model.local.UserPrefrences
import com.example.rifsa_mobile.model.local.dataStore

object Injection {
    fun provideRepostiory(context: Context): MainRepository{
        return MainRepository(UserPrefrences.getInstance(context.dataStore))
    }
}