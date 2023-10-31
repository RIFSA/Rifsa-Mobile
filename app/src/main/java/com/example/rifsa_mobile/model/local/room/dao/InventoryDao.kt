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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertInventoryLocal(data : InventoryEntity)

    @Query("delete from InventoryTable where idInventory like :id")
    fun deleteDiseaseLocal(id: String)

    //read sort data
    @Query("select * from InventoryTable order by name asc " +
            "LIMIT :limit OFFSET :offset")
    fun readInventorySortNameAsc(limit: Int,offset :Int)
    : List<InventoryEntity>

    @Query("select * from InventoryTable order by name desc " +
            "LIMIT :limit OFFSET :offset")
    fun readInventoryNameDesc(limit: Int,offset :Int)
            : List<InventoryEntity>

    @Query("select * from InventoryTable order by day asc " +
            "limit :limit offset :offset")
    fun readInventoryDateAsc(limit: Int,offset :Int)
            : List<InventoryEntity>


    @Query("select * from InventoryTable order by day desc " +
            "limit :limit offset :offset")
    fun readInventoryDateDesc(limit: Int,offset :Int)
            : List<InventoryEntity>


}