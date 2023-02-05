package com.example.rifsa_mobile.view.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.model.repository.local.DiseaseRepository
import com.example.rifsa_mobile.model.repository.local.harvest.HarvestRepository
import com.example.rifsa_mobile.model.repository.local.harvest.IHarvestRepository
import com.example.rifsa_mobile.model.repository.local.preferenceRepository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.example.rifsa_mobile.model.repository.remote.WeatherRepository
import com.google.firebase.database.DatabaseReference

class HomeFragmentViewModel(
    private val harvest : HarvestRepository,
    private val disease : DiseaseRepository,
    private val weatherRepository: WeatherRepository,
    private val preferenceRepository: preferenceRepository
): ViewModel(){
    fun getUserName(): LiveData<String> =
        preferenceRepository.getUserName()

    fun getUserId(): LiveData<String> =
        preferenceRepository.getUserIdKey()

    fun readDiseaseLocal(): LiveData<List<DiseaseEntity>> =
        disease.readLocalDisease()

    suspend fun readHarvestLocal(): LiveData<List<HarvestEntity>> =
        harvest.readHarvestLocal()

    suspend fun getWeatherByLocation(request: UserLocation)
    : LiveData<FetchResult<WeatherDetailResponse>> =
        weatherRepository.getWeatherDataByLocation(request)

    fun getUserLocation(): LiveData<UserLocation> =
        preferenceRepository.getLocationUser()

}