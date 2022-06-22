package com.example.rifsa_mobile.model.remote

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestFirebaseEntity
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FirebaseService {

    fun authLogin(email : String,password : String): Task<AuthResult>{
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    }

    fun insertUpdateHarvestResult(data : HarvestFirebaseEntity, userId : String): Task<Void>{
        return FirebaseDatabase.getInstance()
                .getReference(mainPath)
                .child(userId)
                .child(harvestPath)
                .child(data.date)
                .child(data.id)
                .setValue(data)
    }

    fun queryHarvestResult(userId: String): DatabaseReference{
       return FirebaseDatabase.getInstance()
            .getReference(mainPath)
            .child(userId)
            .child(harvestPath)
    }

    fun deleteHarvestResult(date : String,dataId : String,userId: String): Task<Void>{
        return FirebaseDatabase.getInstance()
            .getReference(mainPath)
            .child(userId)
            .child(harvestPath)
            .child(date)
            .child(dataId)
            .removeValue()
    }

    fun insertUpdateFinancial(data : FinancialFirebaseEntity,userId: String): Task<Void>{
        return FirebaseDatabase.getInstance()
            .getReference(mainPath)
            .child(userId)
            .child(financePath)
            .child(data.date)
            .child(data.idFinance)
            .setValue(data)
    }

    fun queryFinancial(userId: String): DatabaseReference{
        return FirebaseDatabase.getInstance()
            .getReference(mainPath)
            .child(userId)
            .child(financePath)
    }

    fun deleteFinancial(date : String,dataId : String,userId: String): Task<Void>{
        return FirebaseDatabase.getInstance()
            .getReference(mainPath)
            .child(userId)
            .child(financePath)
            .child(date)
            .child(dataId)
            .removeValue()
    }




    companion object{
        const val mainPath = "MainData"
        const val harvestPath = "HarvestResult"
        const val financePath = "Financial"
    }


}