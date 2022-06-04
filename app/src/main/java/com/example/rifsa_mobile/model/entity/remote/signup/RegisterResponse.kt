package com.example.rifsa_mobile.model.entity.remote.signup


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
 @SerializedName("data")
 val registerData: RegisterData,
 @SerializedName("message")
 val message: String,
 @SerializedName("status")
 val status: Int
)

data class RegisterData(
 @SerializedName("createdAt")
 val createdAt: String,
 @SerializedName("email")
 val email: String,
 @SerializedName("id")
 val id: Any,
 @SerializedName("name")
 val name: String,
 @SerializedName("password")
 val password: String,
 @SerializedName("updatedAt")
 val updatedAt: String
)



data class RegisterBody(
  @SerializedName("name")
  val name: String,
  @SerializedName("email")
  val email: String,
  @SerializedName("password")
  val password : String,
  @SerializedName("rePassword")
  val rePassword : String
)