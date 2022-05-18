package com.example.rifsa_mobile.model.entity.harvestresult

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HarvestResult(
    val id_harvest : Int,
    val date : String,
    val title : String,
    val weight : String,
    val jual : Int,
    val catatan : String
): Parcelable

