package com.example.rifsa_mobile.model.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rifsa_mobile.model.entity.local.disease.DiseaseLocal

@Dao
interface DiseaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiseaseLocal(data : DiseaseLocal)

    @Query("select * from DiseaseTable")
    fun getDiseaseLocal(): LiveData<List<DiseaseLocal>>

    @Query("delete from DiseaseTable where id_disease like :id")
    suspend fun deleteDiseaseLocal(id: String)
}