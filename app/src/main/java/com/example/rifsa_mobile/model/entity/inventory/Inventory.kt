package com.example.rifsa_mobile.model.entity.inventory

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Inventory_Table")
data class Inventory(
    @PrimaryKey(autoGenerate = true)
    val id_sort : Int,

    val id_inventories : Int,
    val name : String,
    val amount : Int,
    val urlPhoto : String,
    val noted : String,
    val isUploaded : Boolean
): Parcelable
