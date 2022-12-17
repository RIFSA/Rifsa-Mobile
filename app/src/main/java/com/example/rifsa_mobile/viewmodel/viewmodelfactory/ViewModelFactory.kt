package com.example.rifsa_mobile.viewmodel.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rifsa_mobile.injection.Injection
import com.example.rifsa_mobile.model.repository.local.LocalRepository
import com.example.rifsa_mobile.model.repository.remote.RemoteFirebaseRepository
import com.example.rifsa_mobile.model.repository.remote.RemoteWeatherRepository
import com.example.rifsa_mobile.view.fragment.home.HomeFragmentViewModel
import com.example.rifsa_mobile.view.fragment.setting.SettingFragmentViewModel
import com.example.rifsa_mobile.view.fragment.weather.WeatherFragmentViewModel
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val remoteFirebaseRepository: RemoteFirebaseRepository,
    private val localRepository : LocalRepository,
    private val weatherRepository: RemoteWeatherRepository
    ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(UserPrefrencesViewModel::class.java) ->{
                UserPrefrencesViewModel(localRepository) as T
            }
            modelClass.isAssignableFrom(RemoteViewModel::class.java)->{
                RemoteViewModel(remoteFirebaseRepository) as T
            }
            modelClass.isAssignableFrom(WeatherFragmentViewModel::class.java)->{
                WeatherFragmentViewModel(
                    weatherRepository,
                    localRepository
                ) as T
            }
            modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)->{
                HomeFragmentViewModel(
                    weatherRepository,
                    remoteFirebaseRepository,
                    localRepository
                ) as T
            }
            modelClass.isAssignableFrom(SettingFragmentViewModel::class.java)->{
                SettingFragmentViewModel(localRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var instance : ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(
                    Injection.provideRemoteRepository(),
                    Injection.provideLocalRepository(context),
                    Injection.provideWeatherRepository()
                )
            }.also { instance = it }
    }
}