package com.example.rifsa_mobile.model.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult

@Dao
interface LocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHarvest(data : HarvestResult)

    @Query("select * from Harvest_Table")
    fun getHarvestResult(): LiveData<List<HarvestResult>>

    @Query("delete from Harvest_Table where id_harvest like :id")
    suspend fun deleteHarvest(id : String)
}