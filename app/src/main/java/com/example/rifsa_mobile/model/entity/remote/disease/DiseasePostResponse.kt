package com.example.rifsa_mobile.model.entity.remote.disease


import com.google.gson.annotations.SerializedName

data class DiseasePostResponse(
 @SerializedName("status")
 val status: Int,
 @SerializedName("message")
 val message: String,
 @SerializedName("data")
val DiseasePostDataResponse : DiseasePostDataResponse
)

data class DiseasePostDataResponse(
 @SerializedName("id_penyakit")
 val idPenyakit: Any,
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
)