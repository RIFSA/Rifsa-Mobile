package com.example.rifsa_mobile.model.entity.remotefirebase

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FinancialEntity(
    val idFinance : String = "",
    val date : String = "",
    val name : String = "",
    val price : String = "",
    val type: String = "",
    val noted : String = "",
    var isUploaded : Boolean = false
): Parcelable
