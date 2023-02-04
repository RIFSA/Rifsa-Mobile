package com.example.rifsa_mobile.model.repository.local.financial

import androidx.lifecycle.LiveData
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

interface IFinancialRepository {
    fun insertUpdateFinancial(data : FinancialEntity, userId: String): Task<Void>
    fun readFinancial(userId: String): DatabaseReference
    fun deleteFinancial(date : String,dataId : String, userId : String): Task<Void>
    fun insertFinanceLocally(data : FinancialEntity)
    fun readFinancialLocal(): LiveData<List<FinancialEntity>>
    fun deleteFinancialLocal(localId : Int)
    fun updateFinancialStatus(currentId : String)
    fun readNotUploaded(): List<FinancialEntity>
    fun updateUploadStatus(currentId : String)
}