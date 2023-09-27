package com.example.rifsa_mobile.model.repository.local.financial

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.local.room.dbconfig.DatabaseConfig
import com.example.rifsa_mobile.model.remote.firebase.FirebaseService
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

class FinancialRepository(
    database : DatabaseConfig,
    private var firebase: FirebaseService,
): IFinancialRepository {
    private val dao = database.financialDao()
    private val pagingConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(10)
        .setPageSize(5)
        .build()

    override fun insertUpdateFinancial(date : String,data: FinancialEntity, userId: String): Task<Void> =
        firebase.insertUpdateFinancial(date,data,userId)

    override fun readFinancial(userId: String): DatabaseReference =
        firebase.queryFinancial(userId)

    override fun deleteFinancial(date: String, dataId: String, userId: String): Task<Void> =
        firebase.deleteFinancial(date, dataId, userId)

    override fun insertFinanceLocally(data: FinancialEntity) {
        dao.insertFinanceLocally(data)
    }

    override fun readFinancialLocal(): LiveData<List<FinancialEntity>> {
       return dao.readFinancialLocal()
    }

    override fun deleteFinancialLocal(localId: Int) {
        dao.deleteFinancialLocal(localId)
    }

    override fun updateFinancialStatus(currentId: String) {
        dao.updateFinancialStatus(currentId)
    }

    override fun readNotUploaded(): List<FinancialEntity> =
        dao.readNotUploaded()


    override fun updateUploadStatus(currentId: String) {
        dao.updateUploadStatus(currentId)
    }

    override fun readFinancialPaging(): LiveData<PagedList<FinancialEntity>> {
        val dao = dao.readPagingFinancialLocal()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readPagingFinancialByDateDesc(): LiveData<PagedList<FinancialEntity>> {
        val dao = dao.readPagingFinancialByDateDesc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readFinancialByNameAsc(): LiveData<PagedList<FinancialEntity>> {
        val dao = dao.readFinancialByNameAsc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readFinancialByNameDesc(): LiveData<PagedList<FinancialEntity>> {
        val dao = dao.readFinancialByNameDesc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readFinancialByPriceAsc(): LiveData<PagedList<FinancialEntity>> {
        val dao = dao.readFinancialByPriceAsc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readFinancialByPriceDesc(): LiveData<PagedList<FinancialEntity>> {
        val dao = dao.readFinancialByPriceDesc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readFinancialByDateAsc(): LiveData<PagedList<FinancialEntity>> {
        val dao = dao.readFinancialByDateAsc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }

    override fun readFinancialByDateDesc(): LiveData<PagedList<FinancialEntity>> {
        val dao = dao.readFinancialByDateDesc()
        return LivePagedListBuilder(dao,pagingConfig).build()
    }
}