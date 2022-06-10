package com.example.rifsa_mobile.model.entity.remote.harvestresult


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class HarvestResultRespon(
 @SerializedName("data")
 val harvestResponData: List<HarvestResponData>,
 @SerializedName("message")
 val message: String,
 @SerializedName("status")
 val status: Int
)

@Parcelize
data class HarvestResponData(
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
): Parcelable