package com.example.rifsa_mobile.model.local.room.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryEntity

@Dao
interface InventoryDao {

    //read sort data
    @Query("select * from InventoryTable order by name asc")
    fun readInventorySortNameAsc(): DataSource.Factory<Int, InventoryEntity>

    @Query("select * from InventoryTable order by name desc")
    fun readInventoryNameDesc(): DataSource.Factory<Int, InventoryEntity>

    @Query("select * from InventoryTable order by day asc")
    fun readInventoryDateAsc(): DataSource.Factory<Int, InventoryEntity>

    @Query("select * from InventoryTable order by day desc")
    fun readInventoryDateDesc(): DataSource.Factory<Int, InventoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertInventoryLocal(data : InventoryEntity)

    @Query("delete from InventoryTable where idInventory like :id")
    fun deleteDiseaseLocal(id: String)
}