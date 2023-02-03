package com.example.rifsa_mobile.viewmodel.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rifsa_mobile.injection.Injection
import com.example.rifsa_mobile.model.repository.local.DiseaseRepository
import com.example.rifsa_mobile.model.repository.local.harvest.HarvestRepository
import com.example.rifsa_mobile.model.repository.local.preferenceRepository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.example.rifsa_mobile.model.repository.remote.WeatherRepository
import com.example.rifsa_mobile.view.fragment.disease.viewmodel.DiseaseDetailViewModel
import com.example.rifsa_mobile.view.fragment.disease.viewmodel.DiseaseViewModel
import com.example.rifsa_mobile.view.fragment.harvestresult.HarvestInsertViewModel
import com.example.rifsa_mobile.view.fragment.home.HomeFragmentViewModel
import com.example.rifsa_mobile.view.fragment.setting.SettingViewModel
import com.example.rifsa_mobile.view.fragment.weather.WeatherFragmentViewModel
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val firebaseRepository: FirebaseRepository,
    private val preferenceRepository : preferenceRepository,
    private val diseaseRepository: DiseaseRepository,
    private val weatherRepository: WeatherRepository,
    private val harvestRepository: HarvestRepository,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(HarvestInsertViewModel::class.java)->{
                HarvestInsertViewModel(
                    harvestRepository
                ) as T
            }
            modelClass.isAssignableFrom(UserPrefrencesViewModel::class.java) ->{
                UserPrefrencesViewModel(preferenceRepository) as T
            }
            modelClass.isAssignableFrom(RemoteViewModel::class.java)->{
                RemoteViewModel(firebaseRepository) as T
            }
            modelClass.isAssignableFrom(WeatherFragmentViewModel::class.java)->{
                WeatherFragmentViewModel(
                    weatherRepository,
                    preferenceRepository
                ) as T
            }
            modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)->{
                HomeFragmentViewModel(
                    weatherRepository,
                    firebaseRepository,
                    preferenceRepository
                ) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java)->{
                SettingViewModel(
                    firebaseRepository,
                    diseaseRepository,
                    preferenceRepository
                ) as T
            }
            modelClass.isAssignableFrom(DiseaseDetailViewModel::class.java)->{
                DiseaseDetailViewModel(
                    diseaseRepository,
                    preferenceRepository,
                    firebaseRepository,
                ) as T
            }
            modelClass.isAssignableFrom(DiseaseViewModel::class.java)->{
                DiseaseViewModel(
                    diseaseRepository,
                    firebaseRepository
                ) as T
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
                    Injection.provideFirebaseRepsitory(),
                    Injection.provideLocalRepository(context),
                    Injection.provideDiseaseRepository(context),
                    Injection.provideWeatherRepository(),
                    Injection.provideHarvestRepository(context)
                )
            }.also { instance = it }
    }
}