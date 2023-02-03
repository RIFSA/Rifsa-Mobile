package com.example.rifsa_mobile.model.entity.remotefirebase

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "FinancialTable")
data class FinancialEntity(
    @PrimaryKey(autoGenerate = true)
    val localId : Int = 0,
    val firebaseUserId : String = "",
    val idFinance : String = "",
    val date : String = "",
    val name : String = "",
    val price : String = "",
    val type: String = "",
    val noted : String = "",
    var isUploaded : Boolean = false,
    var uploadReminderId : Int = 0
): Parcelable
