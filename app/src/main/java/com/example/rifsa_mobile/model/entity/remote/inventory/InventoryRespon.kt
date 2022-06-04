package com.example.rifsa_mobile.model.entity.remote.inventory


import com.google.gson.annotations.SerializedName

data class InventoryRespon(
 @SerializedName("data")
 val inventoryResponData: List<InventoryResponData>,
 @SerializedName("message")
 val message: String,
 @SerializedName("status")
 val status: Int
)

data class InventoryResponData(
 @SerializedName("catatan")
 val catatan: String,
 @SerializedName("createdAt")
 val createdAt: String,
 @SerializedName("id_inventaris")
 val idInventaris: Any,
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
)