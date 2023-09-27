package com.example.rifsa_mobile.model.local.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
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

    //paging
    @Query("select*from FinancialTable")
    fun readPagingFinancialLocal(): DataSource.Factory<Int,FinancialEntity>

    @Query("select * from FinancialTable order by day desc")
    fun readPagingFinancialByDateDesc(): DataSource.Factory<Int,FinancialEntity>

    //sort financial
    @Query("select * from FinancialTable order by name asc")
    fun readFinancialByNameAsc(): DataSource.Factory<Int,FinancialEntity>

    @Query("select * from FinancialTable order by name desc")
    fun readFinancialByNameDesc(): DataSource.Factory<Int,FinancialEntity>

    @Query("select * from FinancialTable order by price asc")
    fun readFinancialByPriceAsc(): DataSource.Factory<Int,FinancialEntity>

    @Query("select * from FinancialTable order by price desc")
    fun readFinancialByPriceDesc(): DataSource.Factory<Int,FinancialEntity>

    @Query("select * from FinancialTable order by day asc")
    fun readFinancialByDateAsc(): DataSource.Factory<Int,FinancialEntity>

    @Query("select * from FinancialTable order by day desc")
    fun readFinancialByDateDesc(): DataSource.Factory<Int,FinancialEntity>

}