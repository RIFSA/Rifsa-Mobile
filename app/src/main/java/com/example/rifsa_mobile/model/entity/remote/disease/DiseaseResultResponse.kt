package com.example.rifsa_mobile.model.entity.remote.disease


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class DiseaseResultResponse(
 @SerializedName("data")
 val DiseaseResultDataResponse : List<DiseaseResultDataResponse>,
 @SerializedName("message")
 val message: String,
 @SerializedName("status")
 val status: Int
)

@Parcelize
data class DiseaseResultDataResponse(
 @SerializedName("createdAt")
 val createdAt: String,
 @SerializedName("deskripsi")
 val deskripsi: String,
 @SerializedName("id_penyakit")
 val idPenyakit: Int,
 @SerializedName("image")
 val image: String,
 @SerializedName("indikasi")
 val indikasi: String,
 @SerializedName("latitude")
 val latitude: Double,
 @SerializedName("longitude")
 val longitude: Double,
 @SerializedName("nama")
 val nama: String,
 @SerializedName("updatedAt")
 val updatedAt: String,
 @SerializedName("url")
 val url: String
): Parcelable