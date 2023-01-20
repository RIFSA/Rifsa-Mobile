package com.example.rifsa_mobile.model.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rifsa_mobile.model.entity.local.disease.DiseaseLocal
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity

@Dao
interface DiseaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiseaseLocal(data : DiseaseEntity)

    @Query("select * from DiseaseTable")
    fun getDiseaseLocal(): LiveData<List<DiseaseEntity>>

    @Query("delete from DiseaseTable where id_local like :id")
    suspend fun deleteDiseaseLocal(id: Int)
}