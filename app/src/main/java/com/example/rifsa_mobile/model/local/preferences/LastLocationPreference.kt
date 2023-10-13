package com.example.rifsa_mobile.model.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.entity.preferences.LastLocationPref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStoreLocation : DataStore<Preferences> by preferencesDataStore(
    name = "userLocationPreferences"
)

class LastLocationPreference(private val dataStore : DataStore<Preferences>) {
    private val lastLongitude = doublePreferencesKey("LastLocationLong_key")
    private val lastLatitude = doublePreferencesKey("LastLocationLat_key")

    fun getUserLastLocation(): Flow<LastLocationPref> {
        return dataStore.data.map {
            LastLocationPref(
                it[lastLongitude] ?: 0.0,
                it[lastLatitude] ?: 0.0
            )
        }
    }

    suspend fun saveLastLocation(
        latitude : Double,
        longitude: Double
    ){
        dataStore.edit {
            it[lastLatitude] = latitude
            it[lastLongitude] = longitude
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: LastLocationPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>):LastLocationPreference{
            return INSTANCE ?: synchronized(this){
                val instance = LastLocationPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }

    }
}