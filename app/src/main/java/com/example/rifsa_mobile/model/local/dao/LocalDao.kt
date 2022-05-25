package com.example.rifsa_mobile.model.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rifsa_mobile.model.entity.disase.Disease
import com.example.rifsa_mobile.model.entity.finance.Finance
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult
import com.example.rifsa_mobile.model.entity.inventory.Inventory

@Dao
interface LocalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHarvestLocal(data : HarvestResult)

    @Query("select * from Harvest_Table")
    fun getHarvestLocal(): LiveData<List<HarvestResult>>

    @Query("delete from Harvest_Table where id_harvest like :id")
    suspend fun deleteHarvestLocal(id : String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinanceLocal(data : Finance)

    @Query("select * from Finance_Table")
    fun getFinanceLocal(): LiveData<List<Finance>>

    @Query("delete from Finance_Table where id_finance like :id")
    suspend fun deleteFinanceLocal(id : String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryLocal(data : Inventory)

    @Query("select * from Inventory_Table")
    fun getInventoryLocal(): LiveData<List<Inventory>>

    @Query("delete from Inventory_Table where id_inventories like :id")
    suspend fun deleteInventoryLocal(id : String)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiseaseLocal(data : Disease)

    @Query("select * from Disease_Table")
    fun getDiseaseLocal(): LiveData<List<Disease>>

    @Query("delete from Disease_Table where id_disease like :id")
    suspend fun deleteDiseaseLocal(id: String)






}