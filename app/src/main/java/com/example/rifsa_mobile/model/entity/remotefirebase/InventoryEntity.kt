package com.example.rifsa_mobile.model.entity.remotefirebase

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "InventoryTable")
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true)
    val localId : Int =  0,
    val idInventory: String = "",
    val name: String = "",
    val noted: String = "",
    val imageUrl: String = "",
    val amount: String = "",
    val dated : String = "",
    val day : Int = 0,
    val month : Int = 0,
    val year : Int = 0,
): Parcelable