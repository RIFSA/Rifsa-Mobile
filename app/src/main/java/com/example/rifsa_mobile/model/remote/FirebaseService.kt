package com.example.rifsa_mobile.model.remote

import android.net.Uri
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryFirebaseEntity
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File

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

    fun uploadInventoryFile(name : String, fileUri : Uri, userId: String): UploadTask{
        return FirebaseStorage.getInstance().reference
            .child("$userId/$inventoryFilePath/$name")
            .putFile(fileUri)
    }

    fun insertInventory(data : InventoryFirebaseEntity,userId: String): Task<Void>{
        return FirebaseDatabase.getInstance()
            .getReference(mainPath)
            .child(userId)
            .child(inventoryFilePath)
            .child(data.dated)
            .child(data.idInventory)
            .setValue(data)
    }



    companion object{
        const val mainPath = "MainData"
        const val harvestPath = "HarvestResult"
        const val financePath = "Financial"
        const val inventoryFilePath = "Inventory"
    }


}