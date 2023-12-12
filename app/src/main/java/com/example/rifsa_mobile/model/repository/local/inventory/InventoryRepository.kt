package com.example.rifsa_mobile.model.repository.local.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSourceFactory
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryEntity
import com.example.rifsa_mobile.model.local.room.dbconfig.DatabaseConfig
import com.example.rifsa_mobile.model.repository.utils.RepoUtils

class InventoryRepository(
    private val database : DatabaseConfig
) {
    private val dao = database.inventoryDao()
    private val pagingConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(RepoUtils.initialLoadSize)
        .setPageSize(RepoUtils.pagedSize)
        .build()

    //read sort data
    fun readInventorySortNameAsc(): LiveData<PagingData<InventoryEntity>> {
        return liveData {
            Pager(
                config = PagingConfig(
                    pageSize = 5,
                    enablePlaceholders = true,
                    initialLoadSize = 10
                ),
            ) {
                PagingSource(dao)
            }
        }
    }


    fun readInventorySortNameDesc(): LiveData<PagingData<InventoryEntity>> {
        return liveData {
            Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
            ) {
                PagingSource(dao)
            }
        }
    }

    fun readInventorySortDateAsc(): LiveData<PagingData<InventoryEntity>> {
        return liveData {
            Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
            ) {
                PagingSource(dao)
            }
        }
    }

    fun readInventorySortDateDesc(): LiveData<PagingData<InventoryEntity>> {
        return liveData {
            Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
            ) {
                PagingSource(dao)
            }
        }
    }

    fun insertInventory(data : InventoryEntity){
        dao.insertInventoryLocal(data)
    }

}