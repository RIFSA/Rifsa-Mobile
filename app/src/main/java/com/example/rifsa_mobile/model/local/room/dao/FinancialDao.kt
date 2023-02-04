package com.example.rifsa_mobile.model.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity

@Dao
interface FinancialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFinanceLocally(data : FinancialEntity)

    @Query("select*from FinancialTable")
    fun readFinancialLocal(): LiveData<List<FinancialEntity>>

    @Query("delete from FinancialTable where localId like :localId")
    fun deleteFinancialLocal(localId : Int)

    @Query("update FinancialTable set isUploaded = 1 where idFinance =:currentId")
    fun updateFinancialStatus(currentId : String)

    @Query("select * from FinancialTable where isUploaded = 0")
    fun readNotUploaded(): List<FinancialEntity>

    @Query("update FinancialTable set isUploaded = 1 where idFinance =:currentId")
    fun updateUploadStatus(currentId : String)
}