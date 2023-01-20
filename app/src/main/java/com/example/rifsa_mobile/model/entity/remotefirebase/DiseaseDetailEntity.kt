package com.example.rifsa_mobile.model.entity.remotefirebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiseaseDetailEntity(
    val id : String = "",
    val Name : String = "",
    val Bioname : String = "",
    val Cause : String = "",
    val Causedetail : String = "",
    val Indetfy : String = "",
    val imageUrl : String = ""
): Parcelable