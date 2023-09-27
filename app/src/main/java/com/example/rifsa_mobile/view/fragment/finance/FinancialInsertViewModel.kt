package com.example.rifsa_mobile.view.fragment.finance

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.remote.firebase.FirebaseService
import com.example.rifsa_mobile.model.repository.local.financial.FinancialRepository
import com.example.rifsa_mobile.model.repository.local.financial.IFinancialRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

class FinancialInsertViewModel(
    private var repository: FinancialRepository,
): ViewModel(),IFinancialRepository {
    override fun insertUpdateFinancial(date : String,data: FinancialEntity, userId: String): Task<Void> =
        repository.insertUpdateFinancial(date,data,userId)

    override fun readFinancial(userId: String): DatabaseReference =
        repository.readFinancial(userId)

    override fun deleteFinancial(date: String, dataId: String, userId: String): Task<Void> =
        repository.deleteFinancial(date, dataId, userId)

    override fun insertFinanceLocally(data: FinancialEntity) {
        repository.insertFinanceLocally(data)
    }

    override fun readFinancialLocal(): LiveData<List<FinancialEntity>> =
        repository.readFinancialLocal()

    override fun deleteFinancialLocal(localId: Int) {
        repository.deleteFinancialLocal(localId)
    }

    override fun updateFinancialStatus(currentId: String) {
        repository.updateFinancialStatus(currentId)
    }

    override fun readNotUploaded(): List<FinancialEntity> =
        repository.readNotUploaded()

    override fun updateUploadStatus(currentId: String) {
       repository.updateFinancialStatus(currentId)
    }

    override fun readFinancialPaging(): LiveData<PagedList<FinancialEntity>> {
       return repository.readFinancialPaging()
    }

    override fun readPagingFinancialByDateDesc(): LiveData<PagedList<FinancialEntity>> {
        return repository.readPagingFinancialByDateDesc()
    }

    override fun readFinancialByNameAsc(): LiveData<PagedList<FinancialEntity>> {
       return repository.readFinancialByNameAsc()
    }

    override fun readFinancialByNameDesc(): LiveData<PagedList<FinancialEntity>> {
        return repository.readFinancialByNameDesc()
    }

    override fun readFinancialByPriceAsc(): LiveData<PagedList<FinancialEntity>> {
        return repository.readFinancialByPriceAsc()
    }

    override fun readFinancialByPriceDesc(): LiveData<PagedList<FinancialEntity>> {
        return repository.readFinancialByPriceDesc()
    }

    override fun readFinancialByDateAsc(): LiveData<PagedList<FinancialEntity>> {
        return repository.readFinancialByDateAsc()
    }

    override fun readFinancialByDateDesc(): LiveData<PagedList<FinancialEntity>> {
        return repository.readFinancialByDateDesc()
    }

}