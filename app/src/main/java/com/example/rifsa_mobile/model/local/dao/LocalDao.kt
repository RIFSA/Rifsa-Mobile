package com.example.rifsa_mobile.model.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rifsa_mobile.model.entity.finance.Finance
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult

@Dao
interface LocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHarvest(data : HarvestResult)

    @Query("select * from Harvest_Table")
    fun getHarvestResult(): LiveData<List<HarvestResult>>

    @Query("delete from Harvest_Table where id_harvest like :id")
    suspend fun deleteHarvest(id : String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(data : Finance)

    @Query("select * from Finance_Table")
    fun getFinance(): LiveData<List<Finance>>

    @Query("delete from Finance_Table where id_finance like :id")
    suspend fun deleteFinance(id : String)

}