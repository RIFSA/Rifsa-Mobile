package com.example.rifsa_mobile.model.local.prefrences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow


val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "userPrefrences")

class UserPrefrences(private val dataStore : DataStore<Preferences>) {
    private val onBoardKey  = booleanPreferencesKey("onBoard_key")
    private val nameKey = stringPreferencesKey("name_key")
    private val passKey = stringPreferencesKey("pass_key")
    private val userIdKey = stringPreferencesKey("token_key")


    fun getOnBoardKey(): Flow<Boolean>{
        return dataStore.data.map {
            it[onBoardKey] ?: false
        }
    }

    fun getNameKey(): Flow<String>{
        return dataStore.data.map {
            it[nameKey] ?: ""
        }
    }

    fun getPassKey(): Flow<String>{
        return dataStore.data.map {
            it[passKey] ?: ""
        }
    }

    fun getTokenKey(): Flow<String>{
        return dataStore.data.map {
            it[userIdKey] ?: ""
        }
    }


    suspend fun savePrefrences(onBoard : Boolean,name : String,pass:String, token : String){
        dataStore.edit {
            it[onBoardKey] = onBoard
            it[nameKey] = name
            it[passKey] = pass
            it[userIdKey] = token
        }
    }


    companion object{
        @Volatile
        private var INSTANCE: UserPrefrences? = null

        fun getInstance(dataStore: DataStore<Preferences>):UserPrefrences{
            return INSTANCE ?: synchronized(this){
                val instance = UserPrefrences(dataStore)
                INSTANCE = instance
                instance
            }
        }

    }

}