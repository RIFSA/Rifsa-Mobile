package com.example.rifsa_mobile.view.fragment.harvestresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.model.repository.local.harvest.HarvestRepository
import com.example.rifsa_mobile.model.repository.local.harvest.IHarvestRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

class HarvestInsertViewModel(
    private var repository: HarvestRepository,
): ViewModel(),IHarvestRepository {
    override fun insertUpdateHarvestResult(data : HarvestEntity, userId : String): Task<Void> =
        repository.insertUpdateHarvestResult(data,userId)

    override fun readHarvestResult(userId: String): DatabaseReference =
        repository.readHarvestResult(userId)

    override fun deleteHarvestResult(date : String, dataId : String, userId : String): Task<Void> =
        repository.deleteHarvestResult(date, dataId, userId)

    override fun insertHarvestLocally(data : HarvestEntity) {
        repository.insertHarvestLocally(data)
    }

    override suspend fun readHarvestLocal(): LiveData<List<HarvestEntity>> =
        repository.readHarvestLocal()

    override fun deleteHarvestLocal(localId : Int){
        repository.deleteHarvestLocal(localId)
    }

    override fun updateUploadStatus(currentId : String){
        repository.updateUploadStatus(currentId)
    }

    override fun readNotUploaded(): List<HarvestEntity> =
        repository.readNotUploaded()

}