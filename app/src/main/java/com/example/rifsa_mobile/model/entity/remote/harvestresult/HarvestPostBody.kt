package com.example.rifsa_mobile.model.entity.remote.harvestresult


import com.google.gson.annotations.SerializedName

data class HarvestPostBody(
 @SerializedName("tanggal")
 val tanggal: String,
 @SerializedName("jenis")
 val jenis: String,
 @SerializedName("berat")
 val berat: String,
 @SerializedName("jual")
 val jual: String,
 @SerializedName("catatan")
 val catatan: String,
)

