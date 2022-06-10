package com.example.rifsa_mobile.model.entity.remote.disease


import com.google.gson.annotations.SerializedName

data class DiseasePredictionResponse(
 @SerializedName("result")
 val result: String
)