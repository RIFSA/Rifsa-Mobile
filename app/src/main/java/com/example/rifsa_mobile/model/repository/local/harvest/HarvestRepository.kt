package com.example.rifsa_mobile.model.repository.local.harvest

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.model.local.room.dbconfig.DatabaseConfig
import com.example.rifsa_mobile.model.remote.firebase.FirebaseService
import com.example.rifsa_mobile.model.repository.utils.RepoUtils.initialLoadSize
import com.example.rifsa_mobile.model.repository.utils.RepoUtils.pagedSize
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

class HarvestRepository(
    database : DatabaseConfig,
    private var firebaseService: FirebaseService,
): IHarvestRepository {
    private val dao = database.harvestDao()
    private val pagingConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(initialLoadSize)
        .setPageSize(pagedSize)
        .build()

    override fun insertUpdateHarvestResult(data : HarvestEntity, userId : String): Task<Void> =
        firebaseService.insertUpdateHarvestResult(data,userId)

    override fun readHarvestResult(userId: String): DatabaseReference {
        return firebaseService.queryHarvestResult(userId)
    }

    override fun deleteHarvestResult(date : String, dataId : String, userId : String): Task<Void> {
        return firebaseService.deleteHarvestResult(date, dataId, userId)
    }

    override fun insertHarvestLocally(data : HarvestEntity){
        dao.insertHarvestLocally(data)
    }

    override suspend fun readHarvestLocal(): LiveData<List<HarvestEntity>> =
        dao.readHarvestLocal()

    override fun readNotUploaded(): List<HarvestEntity> =
        dao.readNotUploaded()

    override fun readHarvestByNameAsc(): LiveData<PagedList<HarvestEntity>> {
        val dao = dao.readHarvestByNameAsc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readHarvestByNameDesc(): LiveData<PagedList<HarvestEntity>> {
        val dao = dao.readHarvestByNameAsc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readHarvestByPriceAsc(): LiveData<PagedList<HarvestEntity>> {
        val dao = dao.readHarvestByNameDesc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readHarvestByPriceDesc(): LiveData<PagedList<HarvestEntity>> {
        val dao = dao.readHarvestByPriceDesc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readHarvestByDateAsc(): LiveData<PagedList<HarvestEntity>> {
        val dao = dao.readHarvestByDateAsc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readHarvestByDateDesc(): LiveData<PagedList<HarvestEntity>> {
        val dao = dao.readHarvestByDateDesc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readHarvestByWeightAsc(): LiveData<PagedList<HarvestEntity>> {
        val dao = dao.readHarvestByWeightAsc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readHarvestByWeightDesc(): LiveData<PagedList<HarvestEntity>> {
        val dao = dao.readHarvestByWeightDesc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }


    override fun deleteHarvestLocal(localId : Int){
        dao.deleteHarvestLocal(localId)
    }

    override fun updateUploadStatus(currentId : String){
        dao.updateUploadStatus(currentId)
    }
}