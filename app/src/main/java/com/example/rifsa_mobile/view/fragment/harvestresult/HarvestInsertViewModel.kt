package com.example.rifsa_mobile.view.fragment.harvestresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.model.repository.local.harvest.HarvestRepository
import com.example.rifsa_mobile.model.repository.local.harvest.IHarvestRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

class HarvestInsertViewModel(
    private var harvestRepository: HarvestRepository,
): ViewModel(),IHarvestRepository {
    override fun insertUpdateHarvestResult(data : HarvestEntity, userId : String): Task<Void> =
        harvestRepository.insertUpdateHarvestResult(data,userId)

    override fun readHarvestResult(userId: String): DatabaseReference =
        harvestRepository.readHarvestResult(userId)

    override fun deleteHarvestResult(date : String, dataId : String, userId : String): Task<Void> =
        harvestRepository.deleteHarvestResult(date, dataId, userId)

    override fun insertHarvestLocally(data : HarvestEntity) {
        harvestRepository.insertHarvestLocally(data)
    }

    override suspend fun readHarvestLocal(): LiveData<List<HarvestEntity>> =
        harvestRepository.readHarvestLocal()

    override fun deleteHarvestLocal(localId : Int){
        harvestRepository.deleteHarvestLocal(localId)
    }

    override fun updateHarvestStatus(currentId : String){
        harvestRepository.updateHarvestStatus(currentId)
    }
}