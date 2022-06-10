package com.example.rifsa_mobile.model.entity.remote.disease


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class DiseasePostResponse(
 @SerializedName("status")
 val status: Int,
 @SerializedName("message")
 val message: String,
 @SerializedName("data")
val DiseasePostDataResponse : DiseaseResultDataResponse
)

@Parcelize
data class DiseasePostDataResponse(
 @SerializedName("id_penyakit")
 val idPenyakit: Int,
 @SerializedName("nama")
 val nama: String,
 @SerializedName("image")
 val image: String,
 @SerializedName("url")
 val url: String,
 @SerializedName("indikasi")
 val indikasi: String,
 @SerializedName("latitude")
 val latitude: Double,
 @SerializedName("longitude")
 val longitude: Double,
 @SerializedName("deskripsi")
 val deskripsi: String,
 @SerializedName("updatedAt")
 val updatedAt: String,
 @SerializedName("createdAt")
 val createdAt: String,
): Parcelable