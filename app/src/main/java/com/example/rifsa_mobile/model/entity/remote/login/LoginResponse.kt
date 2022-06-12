package com.example.rifsa_mobile.model.entity.remote.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("token")
    val token: String
)

data class LoginBody(
    @SerializedName("email")
    val email : String,
    @SerializedName("password")
    val password : String
)