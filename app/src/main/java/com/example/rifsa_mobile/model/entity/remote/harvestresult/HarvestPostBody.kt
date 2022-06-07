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

data class HarvestPostResponse(
 @SerializedName("data")
 val harvestResponData: HarvestResponData,
 @SerializedName("message")
 val message: String,
 @SerializedName("status")
 val status: Int
)

data class HarvestPostResponData(
 @SerializedName("berat")
 val berat: String,
 @SerializedName("catatan")
 val catatan: String,
 @SerializedName("createdAt")
 val createdAt: String,
 @SerializedName("id_hasil")
 val idHasil: Int,
 @SerializedName("jenis")
 val jenis: String,
 @SerializedName("jual")
 val jual: String,
 @SerializedName("tanggal")
 val tanggal: String,
 @SerializedName("updatedAt")
 val updatedAt: String
)
