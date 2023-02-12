package com.example.rifsa_mobile.view.fragment.harvestresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.model.repository.local.harvest.HarvestRepository
import com.example.rifsa_mobile.model.repository.local.harvest.IHarvestRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

class HarvestResultViewModel(
    private val repository : HarvestRepository
): ViewModel(){
    suspend fun readHarvestResult(): LiveData<List<HarvestEntity>> =
        repository.readHarvestLocal()

}