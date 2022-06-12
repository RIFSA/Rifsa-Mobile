package com.example.rifsa_mobile.model.entity.remote.disease.restapivm


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class NewDiseaseResultRespon : ArrayList<NewDiseaseResultResponItem>()

@Parcelize
data class NewDiseaseResultResponItem(
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
    @SerializedName("tanggal")
    val tanggal: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("url")
    val url: String
): Parcelable