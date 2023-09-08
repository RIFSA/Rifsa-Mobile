package com.example.rifsa_mobile.model.repository.local.financial

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

interface IFinancialRepository {
    fun insertUpdateFinancial(date : String,data : FinancialEntity, userId: String): Task<Void>
    fun readFinancial(userId: String): DatabaseReference
    fun deleteFinancial(date : String,dataId : String, userId : String): Task<Void>
    fun insertFinanceLocally(data : FinancialEntity)
    fun readFinancialLocal(): LiveData<List<FinancialEntity>>
    fun deleteFinancialLocal(localId : Int)
    fun updateFinancialStatus(currentId : String)
    fun readNotUploaded(): List<FinancialEntity>
    fun updateUploadStatus(currentId : String)

    fun readFinancialByNameAsc(): LiveData<List<FinancialEntity>>

    fun readFinancialByNameDesc(): LiveData<List<FinancialEntity>>

    fun readFinancialByPriceAsc(): LiveData<List<FinancialEntity>>

    fun readFinancialByPriceDesc(): LiveData<List<FinancialEntity>>

    @Query("select * from FinancialTable order by day and month asc")
    fun readFinancialByDateAsc(): LiveData<List<FinancialEntity>>

    @Query("select * from FinancialTable order by day and month desc")
    fun readFinancialByDateDesc(): LiveData<List<FinancialEntity>>
}