package com.example.rifsa_mobile.model.entity.remotefirebase

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class InventoryEntity(
    val idInventory: String = "",
    val name: String = "",
    val noted: String = "",
    val imageUrl: String = "",
    val amount: String = "",
    val dated : String = "",
): Parcelable