package com.example.rifsa_mobile.model.entity.remote.disease


import com.google.gson.annotations.SerializedName

data class NewDiseasePostRespon(
 @SerializedName("data")
 val `data`: NewDiseasePostDataRespon,
 @SerializedName("message")
 val message: String,
 @SerializedName("status")
 val status: Int
)

data class NewDiseasePostDataRespon(
 @SerializedName("result")
 val result: String
)