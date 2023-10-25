package com.example.rifsa_mobile.model.repository.local.inventory

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryEntity
import com.example.rifsa_mobile.model.local.room.dbconfig.DatabaseConfig
import com.example.rifsa_mobile.model.repository.utils.RepoUtils

class InventoryRepository(
    database : DatabaseConfig
) {
    private val dao = database.inventoryDao()
    private val pagingConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(RepoUtils.initialLoadSize)
        .setPageSize(RepoUtils.pagedSize)
        .build()

    //read sort data
    fun readInventorySortNameAsc(): LiveData<PagedList<InventoryEntity>> =
        LivePagedListBuilder(dao.readInventorySortNameAsc(),pagingConfig).build()

    fun readInventorySortNameDesc(): LiveData<PagedList<InventoryEntity>> =
        LivePagedListBuilder(dao.readInventoryNameDesc(),pagingConfig).build()

    fun readInventorySortDateAsc(): LiveData<PagedList<InventoryEntity>> =
        LivePagedListBuilder(dao.readInventoryNameDesc(),pagingConfig).build()

    fun readInventorySortDateDesc(): LiveData<PagedList<InventoryEntity>> =
        LivePagedListBuilder(dao.readInventoryDateAsc(),pagingConfig).build()

    fun insertInventory(data : InventoryEntity){
        dao.insertInventoryLocal(data)
    }

}