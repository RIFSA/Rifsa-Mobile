package com.example.rifsa_mobile.model.repository.local.disease

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.Query
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.local.room.dbconfig.DatabaseConfig
import com.example.rifsa_mobile.model.repository.utils.RepoUtils

class DiseaseRepository(
    database : DatabaseConfig,
) {
    private val dao = database.diseaseDao()
    private val pagingConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(RepoUtils.initialLoadSize)
        .setPageSize(RepoUtils.pagedSize)
        .build()


    fun readLocalDisease(): LiveData<List<DiseaseEntity>> =
        dao.getDiseaseLocal()

    fun getNotUploaded(): List<DiseaseEntity> =
        dao.getNotUploaded()

    fun insertLocalDisease(data : DiseaseEntity) =
        dao.insertDiseaseLocal(data)

    fun deleteLocalDisease(id: String) =
        dao.deleteDiseaseLocal(id)

    fun updateDiseaseUpload(imageUri : Uri, idDisease : String) =
        dao.updateUploadStatus(imageUri.toString(), idDisease)


    //sort data
    fun readDiseaseSortNameAsc(): LiveData<PagedList<DiseaseEntity>> {
        return LivePagedListBuilder(dao.readDiseaseSortNameAsc(),pagingConfig).build()
    }

    fun readDiseaseSortNameDesc(): LiveData<PagedList<DiseaseEntity>>  =
        LivePagedListBuilder(dao.readDiseaseSortNameDesc(),pagingConfig).build()

    fun readDiseaseSortDateAsc(): LiveData<PagedList<DiseaseEntity>>  =
        LivePagedListBuilder(dao.readDiseaseSortDateAsc(),pagingConfig).build()

    fun readDiseaseSortDateDesc(): LiveData<PagedList<DiseaseEntity>>  =
        LivePagedListBuilder(dao.readDiseaseSortDateDesc(),pagingConfig).build()
}