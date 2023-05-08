package com.example.rifsa_mobile.view.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.model.repository.local.financial.FinancialRepository
import com.example.rifsa_mobile.model.repository.local.harvest.HarvestRepository
import com.example.rifsa_mobile.model.repository.local.preference.PreferenceRespository
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val harvest: HarvestRepository,
    private val finance : FinancialRepository,
    private val preferenceRespository : PreferenceRespository,
): ViewModel() {
    suspend fun readHarvestResult(): LiveData<List<HarvestEntity>> =
        harvest.readHarvestLocal()
    fun readFinancial(): LiveData<List<FinancialEntity>> =
        finance.readFinancialLocal()
    fun getUserName(): LiveData<String> = preferenceRespository.getUserName()
    fun saveUserPrefrences(onBoard : Boolean, userName : String, tokenKey: String, userId : String){
        viewModelScope.launch {
            preferenceRespository.savePrefrences(onBoard,userName,tokenKey,userId)
        }
    }
}