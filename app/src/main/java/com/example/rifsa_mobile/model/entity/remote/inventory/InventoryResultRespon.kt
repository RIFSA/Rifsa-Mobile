package com.example.rifsa_mobile.model.entity.remote.inventory


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class InventoryResultRespon(
 @SerializedName("data")
 val InventoryResultResponData: List<InventoryResultResponData>,
 @SerializedName("message")
 val message: String,
 @SerializedName("status")
 val status: Int
)

@Parcelize
data class InventoryResultResponData(
 @SerializedName("catatan")
 val catatan: String,
 @SerializedName("createdAt")
 val createdAt: String,
 @SerializedName("id_inventaris")
 val idInventaris: Int,
 @SerializedName("image")
 val image: String,
 @SerializedName("jumlah")
 val jumlah: String,
 @SerializedName("nama")
 val nama: String,
 @SerializedName("updatedAt")
 val updatedAt: String,
 @SerializedName("url")
 val url: String
):Parcelable