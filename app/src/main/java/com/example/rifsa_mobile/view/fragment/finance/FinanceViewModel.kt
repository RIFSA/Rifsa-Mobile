package com.example.rifsa_mobile.view.fragment.finance

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.repository.local.financial.FinancialRepository
import com.example.rifsa_mobile.model.repository.local.financial.IFinancialRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

class FinanceViewModel(
    private var repository: FinancialRepository
): ViewModel(){

    fun readFinancial(): LiveData<List<FinancialEntity>> =
       repository.readFinancialLocal()
}