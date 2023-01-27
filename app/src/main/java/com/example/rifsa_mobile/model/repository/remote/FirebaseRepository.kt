package com.example.rifsa_mobile.model.repository.remote

import android.net.Uri
import com.example.rifsa_mobile.model.entity.remotefirebase.*
import com.example.rifsa_mobile.model.remote.firebase.FirebaseService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.UploadTask

class FirebaseRepository(private val firebaseService: FirebaseService) {

    fun authLogin(email : String,password : String): Task<AuthResult> =
        firebaseService.authLogin(email, password)

    fun authSignUp(email: String,password: String): Task<AuthResult>{
        return firebaseService.authSignUp(email, password)
    }

    fun insertUpdateHarvestResult(data : HarvestEntity, userId : String): Task<Void> =
        firebaseService.insertUpdateHarvestResult(data,userId)

    fun queryHarvestResult(userId: String): DatabaseReference {
        return firebaseService.queryHarvestResult(userId)
    }

    fun deleteHarvestResult(date : String,dataId : String, userId : String): Task<Void>{
        return firebaseService.deleteHarvestResult(date, dataId, userId)
    }

    fun insertUpdateFinancial(data : FinancialEntity, userId: String): Task<Void>{
        return firebaseService.insertUpdateFinancial(data, userId)
    }

    fun queryFinancial(userId: String): DatabaseReference{
        return firebaseService.queryFinancial(userId)
    }

    fun deleteFinancial(date : String,dataId : String, userId : String): Task<Void>{
        return firebaseService.deleteFinancial(date, dataId, userId)
    }

    fun uploadInventoryFile(name : String, fileUri : Uri, userId: String): UploadTask {
        return firebaseService.uploadInventoryFile(name, fileUri, userId)
    }

    fun insertInventory(data : InventoryEntity, userId: String): Task<Void>{
        return firebaseService.insertInventory(data, userId)
    }

    fun readInventory(userId: String): DatabaseReference{
        return firebaseService.readInventory(userId)
    }

    fun deleteInventoryFile(name : String, userId: String): Task<Void>{
        return firebaseService.deleteInventoryFile(name, userId)
    }

    fun deleteInventory(date : String,dataId : String,userId: String): Task<Void> {
        return firebaseService.deleteInventory(date, dataId, userId)
    }

    fun readFarming(userId: String): DatabaseReference{
        return firebaseService.readFieldData(userId)
    }

    fun insertUpdateFieldData(data : FieldDetailEntity, userId: String): Task<Void>{
        return firebaseService.insertUpdateFieldData(data, userId)
    }

    fun getDiseaseInformation(id : String): DatabaseReference{
        return firebaseService.getDiseaseInformation(id)
    }

    fun getDiseaseInformationMisc(id : String,parent : String): DatabaseReference{
        return firebaseService.getDiseaseInformationMisc(id, parent)
    }

    fun uploadDiseaseImage(idDisease : String, fileUri : Uri, userId: String): UploadTask{
        return firebaseService.uploadDiseaseImage(idDisease, fileUri, userId)
    }

    fun saveDisease(data : DiseaseEntity, userId: String): Task<Void>{
        return firebaseService.saveDisease(data, userId)
    }

    fun readDiseaseList(userId : String): DatabaseReference{
        return firebaseService.readDiseaseList(userId)
    }

    fun deleteDisease(date : String,dataId : String,userId: String): Task<Void> {
        return firebaseService.deleteDisease(date, dataId, userId)
    }

    fun deleteDiseaseImage(name : String, userId: String): Task<Void>{
        return firebaseService.deleteDiseaseImage(name, userId)
    }

    fun getDiseaseWiki(): DatabaseReference{
        return firebaseService.getDiseaseWiki()
    }

}