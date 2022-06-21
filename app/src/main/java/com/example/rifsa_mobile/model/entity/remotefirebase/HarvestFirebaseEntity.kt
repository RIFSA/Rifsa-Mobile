package com.example.rifsa_mobile.model.entity.remotefirebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class HarvestFirebaseEntity(
    var id : String="",
    var date : String="",
    var typeOfGrain : String="",
    var weight : String="",
    var income : String="",
    var note : String="",
    var isUploaded : Boolean = true
): Parcelable