package com.example.rifsa_mobile.model.repository.local.disease

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.local.room.dbconfig.DatabaseConfig

class DiseaseRepository(
    database : DatabaseConfig,
) {
    private val dao = database.diseaseDao()

    fun readLocalDisease(): LiveData<List<DiseaseEntity>> =
        dao.getDiseaseLocal()

    fun getNotUploaded(): List<DiseaseEntity> =
        dao.getNotUploaded()

    fun getDiseaseNotUploaded(): LiveData<List<DiseaseEntity>> =
        dao.getDiseaseNotUploaded()

    fun checkNotUploaded(): List<DiseaseEntity> =
        dao.getNotUploaded()

    fun getDataNotUploaded(alarmId : Int): DiseaseEntity =
        dao.getDataNotUploaded(alarmId)

    fun insertLocalDisease(data : DiseaseEntity) =
        dao.insertDiseaseLocal(data)

    fun deleteLocalDisease(id: String) =
        dao.deleteDiseaseLocal(id)

    fun updateDiseaseUpload(imageUri : Uri, idDisease : String) =
        dao.updateUploadStatus(imageUri.toString(), idDisease)
}