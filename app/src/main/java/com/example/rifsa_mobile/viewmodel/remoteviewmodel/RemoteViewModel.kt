package com.example.rifsa_mobile.viewmodel.remoteviewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.remotefirebase.*
import com.example.rifsa_mobile.model.repository.RemoteRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.UploadTask

class RemoteViewModel(private val remoteRepository: RemoteRepository): ViewModel() {


    fun authLogin(email : String,password : String): Task<AuthResult> =
        remoteRepository.authLogin(email, password)

    fun authSignUp(email: String,password: String): Task<AuthResult>{
        return remoteRepository.authSignUp(email, password)
    }

    fun insertUpdateHarvestResult(data : HarvestFirebaseEntity, userId : String): Task<Void> =
        remoteRepository.insertUpdateHarvestResult(data, userId)

    fun readHarvestResult(userId: String): DatabaseReference =
        remoteRepository.queryHarvestResult(userId)


    fun deleteHarvestResult(date : String,dataId : String, userId : String): Task<Void> =
        remoteRepository.deleteHarvestResult(date, dataId, userId)

    fun insertUpdateFinancial(data : FinancialFirebaseEntity, userId: String): Task<Void> =
        remoteRepository.insertUpdateFinancial(data, userId)

    fun readFinancial(userId: String): DatabaseReference =
        remoteRepository.queryFinancial(userId)

    fun deleteFinancial(date : String,dataId : String, userId : String): Task<Void> =
        remoteRepository.deleteFinancial(date, dataId, userId)

    fun uploadInventoryFile(name : String, fileUri : Uri, userId: String): UploadTask =
        remoteRepository.uploadInventoryFile(name, fileUri, userId)

    fun insertInventory(data : InventoryFirebaseEntity, userId: String): Task<Void>{
        return remoteRepository.insertInventory(data, userId)
    }

    fun readInventory(userId: String): DatabaseReference{
        return remoteRepository.readInventory(userId)
    }

    fun deleteInventoryFile(name : String, userId: String): Task<Void>{
        return remoteRepository.deleteInventoryFile(name, userId)
    }

    fun deleteInventory(date : String,dataId : String,userId: String): Task<Void> {
        return remoteRepository.deleteInventory(date, dataId, userId)
    }

    fun readFarming(userId: String): DatabaseReference{
        return remoteRepository.readFarming(userId)
    }

    fun insertUpdateFieldData(data : FieldFirebaseEntity, userId: String): Task<Void>{
        return remoteRepository.insertUpdateFieldData(data, userId)
    }

    fun getDiseaseInformation(id : String): DatabaseReference{
        return remoteRepository.getDiseaseInformation(id)
    }

    fun getDiseaseInformationMisc(id : String,parent : String): DatabaseReference{
        return remoteRepository.getDiseaseInformationMisc(id, parent)
    }

    fun uploadDiseaseImage(name : String, fileUri : Uri, userId: String): UploadTask{
        return remoteRepository.uploadDiseaseImage(name, fileUri, userId)
    }

    fun saveDisease(data : DiseaseFirebaseEntity, userId: String): Task<Void>{
        return remoteRepository.saveDisease(data, userId)
    }

    fun readDiseaseList(userId : String): DatabaseReference{
        return remoteRepository.readDiseaseList(userId)
    }

    fun deleteDisease(date : String,dataId : String,userId: String): Task<Void> {
        return remoteRepository.deleteDisease(date, dataId, userId)
    }

    fun deleteDiseaseImage(name : String, userId: String): Task<Void>{
        return remoteRepository.deleteDiseaseImage(name, userId)
    }
}