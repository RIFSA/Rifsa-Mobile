package com.example.rifsa_mobile.model.entity.harvestresult

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HarvestResult(
    val title : String,
    val weight : String,
    val date : String
): Parcelable

