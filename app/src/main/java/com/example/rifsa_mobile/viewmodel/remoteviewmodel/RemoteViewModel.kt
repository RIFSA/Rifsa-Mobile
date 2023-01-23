package com.example.rifsa_mobile.viewmodel.remoteviewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.remotefirebase.*
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.UploadTask

class RemoteViewModel(private val firebaseRepository: FirebaseRepository): ViewModel() {

    fun authLogin(email : String,password : String): Task<AuthResult> =
        firebaseRepository.authLogin(email, password)

    fun authSignUp(email: String,password: String): Task<AuthResult>{
        return firebaseRepository.authSignUp(email, password)
    }

    fun insertUpdateHarvestResult(data : HarvestEntity, userId : String): Task<Void> =
        firebaseRepository.insertUpdateHarvestResult(data, userId)

    fun readHarvestResult(userId: String): DatabaseReference =
        firebaseRepository.queryHarvestResult(userId)

    fun deleteHarvestResult(date : String,dataId : String, userId : String): Task<Void> =
        firebaseRepository.deleteHarvestResult(date, dataId, userId)

    fun insertUpdateFinancial(data : FinancialEntity, userId: String): Task<Void> =
        firebaseRepository.insertUpdateFinancial(data, userId)

    fun readFinancial(userId: String): DatabaseReference =
        firebaseRepository.queryFinancial(userId)

    fun deleteFinancial(date : String,dataId : String, userId : String): Task<Void> =
        firebaseRepository.deleteFinancial(date, dataId, userId)

    fun uploadInventoryFile(name : String, fileUri : Uri, userId: String): UploadTask =
        firebaseRepository.uploadInventoryFile(name, fileUri, userId)

    fun insertInventory(data : InventoryEntity, userId: String): Task<Void>{
        return firebaseRepository.insertInventory(data, userId)
    }

    fun readInventory(userId: String): DatabaseReference{
        return firebaseRepository.readInventory(userId)
    }

    fun deleteInventoryFile(name : String, userId: String): Task<Void>{
        return firebaseRepository.deleteInventoryFile(name, userId)
    }

    fun deleteInventory(date : String,dataId : String,userId: String): Task<Void> {
        return firebaseRepository.deleteInventory(date, dataId, userId)
    }

    fun readFarming(userId: String): DatabaseReference{
        return firebaseRepository.readFarming(userId)
    }

    fun insertUpdateFieldData(data : FieldDetailEntity, userId: String): Task<Void>{
        return firebaseRepository.insertUpdateFieldData(data, userId)
    }

    fun getDiseaseInformation(id : String): DatabaseReference{
        return firebaseRepository.getDiseaseInformation(id)
    }

    fun getDiseaseInformationMisc(id : String,parent : String): DatabaseReference{
        return firebaseRepository.getDiseaseInformationMisc(id, parent)
    }

    fun uploadDiseaseImage(name : String, fileUri : Uri, userId: String): UploadTask{
        return firebaseRepository.uploadDiseaseImage(name, fileUri, userId)
    }

    fun saveDisease(data : DiseaseEntity, userId: String): Task<Void>{
        return firebaseRepository.saveDisease(data, userId)
    }

    fun readDiseaseList(userId : String): DatabaseReference{
        return firebaseRepository.readDiseaseList(userId)
    }

    fun deleteDisease(date : String,dataId : String,userId: String): Task<Void> {
        return firebaseRepository.deleteDisease(date, dataId, userId)
    }

    fun deleteDiseaseImage(name : String, userId: String): Task<Void>{
        return firebaseRepository.deleteDiseaseImage(name, userId)
    }

    fun getDiseaseWiki(): DatabaseReference{
        return firebaseRepository.getDiseaseWiki()
    }
}