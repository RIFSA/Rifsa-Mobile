package com.example.rifsa_mobile.model.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity

@Dao
interface HarvestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHarvestLocally(data : HarvestEntity)

    @Query("select*from HarvestTable")
    fun readHarvestLocal(): LiveData<List<HarvestEntity>>

    @Query("delete from HarvestTable where localId like :localId")
    fun deleteHarvestLocal(localId : Int)

    @Query("update HarvestTable set isUploaded = 1 where id =:currentId")
    fun updateHarvestStatus(currentId : String)
}