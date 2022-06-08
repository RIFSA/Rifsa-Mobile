package com.example.rifsa_mobile.model.entity.remote.disease


import com.google.gson.annotations.SerializedName

data class DiseaseResultResponse(
 @SerializedName("data")
 val DiseaseResultDataResponse : List<DiseaseResultDataResponse>,
 @SerializedName("message")
 val message: String,
 @SerializedName("status")
 val status: Int
)

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
 val latitude: Int,
 @SerializedName("longitude")
 val longitude: Int,
 @SerializedName("nama")
 val nama: String,
 @SerializedName("tanggal")
 val tanggal: String,
 @SerializedName("updatedAt")
 val updatedAt: String,
 @SerializedName("url")
 val url: String
)