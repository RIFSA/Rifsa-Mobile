package com.example.rifsa_mobile.model.entity.remote.inventory

import com.google.gson.annotations.SerializedName

data class InventoryPostResponse(
    @SerializedName("data")
    val inventoryResponData: InventoryPostResponData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)

data class InventoryPostResponData(
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
