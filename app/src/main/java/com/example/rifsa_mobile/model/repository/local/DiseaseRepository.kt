package com.example.rifsa_mobile.model.repository.local

import androidx.lifecycle.LiveData
import com.example.rifsa_mobile.model.entity.local.disease.DiseaseLocal
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.local.room.dbconfig.DbConfig

class DiseaseRepository(
    private val database : DbConfig,
) {
    private val dao = database.diseaseDao()

    fun readLocalDisease(): LiveData<List<DiseaseEntity>> =
        dao.getDiseaseLocal()

    suspend fun insertLocalDisease(data : DiseaseEntity){
        dao.insertDiseaseLocal(data)
    }

    suspend fun deleteLocalDisease(id: Int){
        dao.deleteDiseaseLocal(id)
    }
}