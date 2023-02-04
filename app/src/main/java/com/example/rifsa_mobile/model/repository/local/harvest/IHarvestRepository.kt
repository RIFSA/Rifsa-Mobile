package com.example.rifsa_mobile.model.repository.local.harvest

import androidx.lifecycle.LiveData
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

interface IHarvestRepository {
    fun insertUpdateHarvestResult(data : HarvestEntity, userId : String): Task<Void>
    fun readHarvestResult(userId: String): DatabaseReference
    fun deleteHarvestResult(date : String,dataId : String, userId : String): Task<Void>
    fun insertHarvestLocally(data : HarvestEntity)
    suspend fun readHarvestLocal(): LiveData<List<HarvestEntity>>
    fun deleteHarvestLocal(localId : Int)
    fun updateUploadStatus(currentId : String)
    fun readNotUploaded(): List<HarvestEntity>
}