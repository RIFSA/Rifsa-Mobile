package com.example.rifsa_mobile.model.entity.finance

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Finance_Table")
data class Finance(
    @PrimaryKey(autoGenerate = true)
    val id_sort : Int,

    val id_finance : String,
    val date : String,
    val title : String,
    val type : String,
    val note : String,
    val amount : Int,

    val isUploaded : Boolean
): Parcelable
