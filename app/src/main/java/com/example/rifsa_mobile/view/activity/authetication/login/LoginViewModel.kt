package com.example.rifsa_mobile.view.activity.authetication.login

import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.model.repository.local.disease.DiseaseRepository
import com.example.rifsa_mobile.model.repository.local.financial.FinancialRepository
import com.example.rifsa_mobile.model.repository.local.harvest.HarvestRepository
import com.example.rifsa_mobile.model.repository.local.preference.PreferenceRespository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference

class LoginViewModel(
    private val preference : PreferenceRespository,
    private val firebaseRepository: FirebaseRepository,
    private val harvestRepository: HarvestRepository,
    private val diseaseRepository: DiseaseRepository,
    private val financialRepository: FinancialRepository
): ViewModel() {

    suspend fun saveUserPreferences(
        onBoard : Boolean,
        name : String,
        pass: String,
        userId : String
    ){
        preference.savePrefrences(onBoard,name,pass,userId)
    }

    fun authLogin(email : String,password : String): Task<AuthResult> =
        firebaseRepository.authLogin(email, password)

    fun readHarvestResult(userId: String): DatabaseReference =
        firebaseRepository.queryHarvestResult(userId)

    fun insertHarvestLocal(data : HarvestEntity){
        harvestRepository.insertHarvestLocally(data)
    }

    fun readDiseaseRemote(userId: String): DatabaseReference =
        firebaseRepository.readDiseaseList(userId)

    fun insertDiseaseLocal(data : DiseaseEntity){
        diseaseRepository.insertLocalDisease(data)
    }

    fun readFinancialRemote(userId: String): DatabaseReference =
        firebaseRepository.queryFinancial(userId)

    fun insertFinancialLocal(data : FinancialEntity){
        financialRepository.insertFinanceLocally(data)
    }
}