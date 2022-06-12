package com.example.rifsa_mobile.model.entity.remote.disease.restapivm


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class NewDiseaseResultRespon : ArrayList<DiseaseResultResponse>()

@Parcelize
data class DiseaseResultResponse(
    @SerializedName("createdAt")
    val createdAt: String,
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
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("url")
    val url: String
): Parcelable