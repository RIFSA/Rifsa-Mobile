package com.example.rifsa_mobile.model.entity.remotefirebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DiseaseFirebaseEntity(
    var id : String = "",
    var nameDisease : String = "",
    var idDisease : String = "",
    var dateDisease : String = "",
    var latitude : String = "",
    var longitude : String = "",
    var imageUrl : String = ""
): Parcelable