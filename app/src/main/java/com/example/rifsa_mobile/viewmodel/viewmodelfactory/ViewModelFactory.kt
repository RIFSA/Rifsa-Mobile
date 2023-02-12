package com.example.rifsa_mobile.viewmodel.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rifsa_mobile.injection.Injection
import com.example.rifsa_mobile.model.repository.local.disease.DiseaseRepository
import com.example.rifsa_mobile.model.repository.local.financial.FinancialRepository
import com.example.rifsa_mobile.model.repository.local.harvest.HarvestRepository
import com.example.rifsa_mobile.model.repository.local.preference.PreferenceRespository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.example.rifsa_mobile.model.repository.remote.WeatherRepository
import com.example.rifsa_mobile.view.activity.authetication.login.LoginViewModel
import com.example.rifsa_mobile.view.fragment.disease.viewmodel.DiseaseDetailViewModel
import com.example.rifsa_mobile.view.fragment.disease.viewmodel.DiseaseViewModel
import com.example.rifsa_mobile.view.fragment.finance.FinanceViewModel
import com.example.rifsa_mobile.view.fragment.finance.FinancialInsertViewModel
import com.example.rifsa_mobile.view.fragment.harvestresult.HarvestInsertViewModel
import com.example.rifsa_mobile.view.fragment.harvestresult.HarvestResultViewModel
import com.example.rifsa_mobile.view.fragment.home.HomeFragmentViewModel
import com.example.rifsa_mobile.view.fragment.setting.SettingViewModel
import com.example.rifsa_mobile.view.fragment.weather.WeatherFragmentViewModel
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
    private val firebaseRepository: FirebaseRepository,
    private val PreferenceRespository : PreferenceRespository,
    private val diseaseRepository: DiseaseRepository,
    private val weatherRepository: WeatherRepository,
    private val harvestRepository: HarvestRepository,
    private val FinancialRepository : FinancialRepository,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(HarvestInsertViewModel::class.java)->{
                HarvestInsertViewModel(
                    harvestRepository
                ) as T
            }
            modelClass.isAssignableFrom(HarvestResultViewModel::class.java)->{
                HarvestResultViewModel(
                    harvestRepository
                ) as T
            }
            modelClass.isAssignableFrom(FinancialInsertViewModel::class.java)->{
                FinancialInsertViewModel(
                    FinancialRepository,
                ) as T
            }
            modelClass.isAssignableFrom(FinanceViewModel::class.java)->{
                FinanceViewModel(
                    FinancialRepository
                ) as T
            }
            modelClass.isAssignableFrom(UserPrefrencesViewModel::class.java) ->{
                UserPrefrencesViewModel(PreferenceRespository) as T
            }
            modelClass.isAssignableFrom(RemoteViewModel::class.java)->{
                RemoteViewModel(firebaseRepository) as T
            }
            modelClass.isAssignableFrom(WeatherFragmentViewModel::class.java)->{
                WeatherFragmentViewModel(
                    weatherRepository,
                    PreferenceRespository
                ) as T
            }
            modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)->{
                HomeFragmentViewModel(
                    harvestRepository,
                    diseaseRepository,
                    weatherRepository,
                    PreferenceRespository
                ) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java)->{
                SettingViewModel(
                    firebaseRepository,
                    diseaseRepository,
                    PreferenceRespository
                ) as T
            }
            modelClass.isAssignableFrom(DiseaseDetailViewModel::class.java)->{
                DiseaseDetailViewModel(
                    diseaseRepository,
                    PreferenceRespository,
                    firebaseRepository,
                ) as T
            }
            modelClass.isAssignableFrom(DiseaseViewModel::class.java)->{
                DiseaseViewModel(
                    diseaseRepository,
                    firebaseRepository
                ) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java)->{
                LoginViewModel(
                    PreferenceRespository,
                    firebaseRepository,
                    harvestRepository,
                    diseaseRepository,
                    FinancialRepository
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
                    Injection.provideHarvestRepository(context),
                    Injection.provideFinancialRepository(context)
                )
            }.also { instance = it }
    }
}