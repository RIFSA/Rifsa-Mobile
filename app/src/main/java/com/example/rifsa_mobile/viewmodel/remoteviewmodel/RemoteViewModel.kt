package com.example.rifsa_mobile.viewmodel.remoteviewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.remotefirebase.*
import com.example.rifsa_mobile.model.repository.remote.RemoteFirebaseRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.UploadTask

class RemoteViewModel(private val remoteFirebaseRepository: RemoteFirebaseRepository): ViewModel() {

    fun authLogin(email : String,password : String): Task<AuthResult> =
        remoteFirebaseRepository.authLogin(email, password)

    fun authSignUp(email: String,password: String): Task<AuthResult>{
        return remoteFirebaseRepository.authSignUp(email, password)
    }

    fun insertUpdateHarvestResult(data : HarvestFirebaseEntity, userId : String): Task<Void> =
        remoteFirebaseRepository.insertUpdateHarvestResult(data, userId)

    fun readHarvestResult(userId: String): DatabaseReference =
        remoteFirebaseRepository.queryHarvestResult(userId)

    fun deleteHarvestResult(date : String,dataId : String, userId : String): Task<Void> =
        remoteFirebaseRepository.deleteHarvestResult(date, dataId, userId)

    fun insertUpdateFinancial(data : FinancialFirebaseEntity, userId: String): Task<Void> =
        remoteFirebaseRepository.insertUpdateFinancial(data, userId)

    fun readFinancial(userId: String): DatabaseReference =
        remoteFirebaseRepository.queryFinancial(userId)

    fun deleteFinancial(date : String,dataId : String, userId : String): Task<Void> =
        remoteFirebaseRepository.deleteFinancial(date, dataId, userId)

    fun uploadInventoryFile(name : String, fileUri : Uri, userId: String): UploadTask =
        remoteFirebaseRepository.uploadInventoryFile(name, fileUri, userId)

    fun insertInventory(data : InventoryFirebaseEntity, userId: String): Task<Void>{
        return remoteFirebaseRepository.insertInventory(data, userId)
    }

    fun readInventory(userId: String): DatabaseReference{
        return remoteFirebaseRepository.readInventory(userId)
    }

    fun deleteInventoryFile(name : String, userId: String): Task<Void>{
        return remoteFirebaseRepository.deleteInventoryFile(name, userId)
    }

    fun deleteInventory(date : String,dataId : String,userId: String): Task<Void> {
        return remoteFirebaseRepository.deleteInventory(date, dataId, userId)
    }

    fun readFarming(userId: String): DatabaseReference{
        return remoteFirebaseRepository.readFarming(userId)
    }

    fun insertUpdateFieldData(data : FieldFirebaseEntity, userId: String): Task<Void>{
        return remoteFirebaseRepository.insertUpdateFieldData(data, userId)
    }

    fun getDiseaseInformation(id : String): DatabaseReference{
        return remoteFirebaseRepository.getDiseaseInformation(id)
    }

    fun getDiseaseInformationMisc(id : String,parent : String): DatabaseReference{
        return remoteFirebaseRepository.getDiseaseInformationMisc(id, parent)
    }

    fun uploadDiseaseImage(name : String, fileUri : Uri, userId: String): UploadTask{
        return remoteFirebaseRepository.uploadDiseaseImage(name, fileUri, userId)
    }

    fun saveDisease(data : DiseaseFirebaseEntity, userId: String): Task<Void>{
        return remoteFirebaseRepository.saveDisease(data, userId)
    }

    fun readDiseaseList(userId : String): DatabaseReference{
        return remoteFirebaseRepository.readDiseaseList(userId)
    }

    fun deleteDisease(date : String,dataId : String,userId: String): Task<Void> {
        return remoteFirebaseRepository.deleteDisease(date, dataId, userId)
    }

    fun deleteDiseaseImage(name : String, userId: String): Task<Void>{
        return remoteFirebaseRepository.deleteDiseaseImage(name, userId)
    }

    fun getDiseaseWiki(): DatabaseReference{
        return remoteFirebaseRepository.getDiseaseWiki()
    }
}