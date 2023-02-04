package com.example.rifsa_mobile.model.repository.local.harvest

import androidx.lifecycle.LiveData
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.model.local.room.dbconfig.DatabaseConfig
import com.example.rifsa_mobile.model.remote.firebase.FirebaseService
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

class HarvestRepository(
    database : DatabaseConfig,
    private var firebaseService: FirebaseService,
): IHarvestRepository {
    private val dao = database.harvestDao()

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


    override fun deleteHarvestLocal(localId : Int){
        dao.deleteHarvestLocal(localId)
    }

    override fun updateUploadStatus(currentId : String){
        dao.updateUploadStatus(currentId)
    }
}