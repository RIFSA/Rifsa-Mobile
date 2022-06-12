package com.example.rifsa_mobile.model.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rifsa_mobile.model.entity.local.disase.Disease

@Dao
interface LocalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiseaseLocal(data : Disease)

    @Query("select * from Disease_Table")
    fun getDiseaseLocal(): LiveData<List<Disease>>

    @Query("delete from Disease_Table where id_sort like :id")
    suspend fun deleteDiseaseLocal(id: String)

}