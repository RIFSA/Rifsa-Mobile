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
    val day : Int = 0,
    val month : Int = 0,
    val year : Int = 0,
    val name : String = "",
    val price : Int = 0,
    val type: String = "",
    val noted : String = "",
    var isUploaded : Boolean = false,
    var uploadReminderId : Int = 0
): Parcelable
