package com.example.rifsa_mobile.model.entity.local.harvestresult

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@kotlinx.parcelize.Parcelize
@Entity(tableName = "Harvest_Table")
data class HarvestResult(
    @PrimaryKey(autoGenerate = true)
    val id_sort : Int,

    val id_harvest : String,
    val date : String,
    val title : String,
    val weight : Int,
    val sellingPrice : Int,
    val noted : String,

    val isUploaded : Boolean
): Parcelable

