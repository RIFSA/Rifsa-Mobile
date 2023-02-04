package com.example.rifsa_mobile.model.repository.local.financial

import androidx.lifecycle.LiveData
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.local.room.dbconfig.DatabaseConfig
import com.example.rifsa_mobile.model.remote.firebase.FirebaseService
import com.example.rifsa_mobile.model.repository.remote.FirebaseRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

class FinancialRepository(
    database : DatabaseConfig,
    private var firebase: FirebaseService,
): IFinancialRepository {
    val dao = database.financialDao()

    override fun insertUpdateFinancial(data: FinancialEntity, userId: String): Task<Void> =
        firebase.insertUpdateFinancial(data,userId)

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
}