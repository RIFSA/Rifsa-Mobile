package com.example.rifsa_mobile.model.entity.remote.disease


import com.google.gson.annotations.SerializedName

data class DiseasePostResponse(
 @SerializedName("data")
 val DiseasePostDataResponse : DiseasePostDataResponse,
 @SerializedName("message")
 val message: String,
 @SerializedName("status")
 val status: Int
)

data class DiseasePostDataResponse(
 @SerializedName("createdAt")
 val createdAt: String,
 @SerializedName("deskripsi")
 val deskripsi: String,
 @SerializedName("id_penyakit")
 val idPenyakit: Any,
 @SerializedName("image")
 val image: String,
 @SerializedName("indikasi")
 val indikasi: String,
 @SerializedName("latitude")
 val latitude: String,
 @SerializedName("longitude")
 val longitude: String,
 @SerializedName("nama")
 val nama: String,
 @SerializedName("tanggal")
 val tanggal: String,
 @SerializedName("updatedAt")
 val updatedAt: String,
 @SerializedName("url")
 val url: String
)