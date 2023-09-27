package com.example.rifsa_mobile.model.local.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity

@Dao
interface HarvestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHarvestLocally(data : HarvestEntity)

    @Query("select*from HarvestTable")
    fun readHarvestLocal(): LiveData<List<HarvestEntity>>

    @Query("delete from HarvestTable where localId like :localId")
    fun deleteHarvestLocal(localId : Int)

    @Query("select * from HarvestTable where isUploaded = 0")
    fun readNotUploaded(): List<HarvestEntity>

    @Query("update HarvestTable set isUploaded = 1 where id =:currentId")
    fun updateUploadStatus(currentId : String)


    //sort harvest
    @Query("select * from HarvestTable order by typeOfGrain asc")
    fun readHarvestByNameAsc(): DataSource.Factory<Int, HarvestEntity>

    @Query("select * from HarvestTable order by typeOfGrain desc")
    fun readHarvestByNameDesc(): DataSource.Factory<Int, HarvestEntity>

    @Query("select * from HarvestTable order by income asc")
    fun readHarvestByPriceAsc(): DataSource.Factory<Int, HarvestEntity>

    @Query("select * from HarvestTable order by income desc")
    fun readHarvestByPriceDesc(): DataSource.Factory<Int, HarvestEntity>

    @Query("select * from HarvestTable order by day asc")
    fun readHarvestByDateAsc(): DataSource.Factory<Int, HarvestEntity>

    @Query("select * from HarvestTable order by day desc")
    fun readHarvestByDateDesc(): DataSource.Factory<Int, HarvestEntity>

    @Query("select * from HarvestTable order by weight asc")
    fun readHarvestByWeightAsc(): DataSource.Factory<Int, HarvestEntity>

    @Query("select * from HarvestTable order by weight desc")
    fun readHarvestByWeightDesc(): DataSource.Factory<Int, HarvestEntity>

}