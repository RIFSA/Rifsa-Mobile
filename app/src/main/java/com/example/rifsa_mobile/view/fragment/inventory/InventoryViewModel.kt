package com.example.rifsa_mobile.view.fragment.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryEntity
import com.example.rifsa_mobile.model.repository.local.inventory.InventoryRepository
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository

class InventoryViewModel(
    private val inventoryRepository: InventoryRepository,
    private val remoteRepository : FirebaseRepository
): ViewModel() {

    fun readInventorySortNameDesc(): LiveData<PagedList<InventoryEntity>> =
       inventoryRepository.readInventorySortNameDesc()

    fun readInventorySortNameAsc(): LiveData<PagedList<InventoryEntity>> =
        inventoryRepository.readInventorySortNameAsc()

    fun readInventorySortDateAsc(): LiveData<PagedList<InventoryEntity>> =
        inventoryRepository.readInventorySortDateAsc()

    fun readInventorySortDateDesc(): LiveData<PagedList<InventoryEntity>> =
        inventoryRepository.readInventorySortDateDesc()

    fun insertInventory(data : InventoryEntity){
        inventoryRepository.insertInventory(data)
    }
}