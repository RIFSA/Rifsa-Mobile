package com.example.rifsa_mobile.model.entity.remotefirebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FieldDetailEntity(
    val idField : String = "",
    val name : String = "",
    val owner : String = "",
    val address : String = "",
    val production: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val date: String = "",
): Parcelable