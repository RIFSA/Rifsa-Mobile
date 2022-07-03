package com.example.rifsa_mobile.model.remote

import com.example.rifsa_mobile.model.entity.remote.disease.DiseasePostResponse
import com.example.rifsa_mobile.model.entity.remote.disease.restapivm.NewDiseaseResultRespon

import com.example.rifsa_mobile.model.entity.remote.login.LoginBody
import com.example.rifsa_mobile.model.entity.remote.login.LoginResponse
import com.example.rifsa_mobile.model.entity.remote.signup.RegisterBody
import com.example.rifsa_mobile.model.entity.remote.signup.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun postLogin(
        @Body body: LoginBody
    ): LoginResponse

    @POST("register")
    suspend fun postRegister(
        @Body body: RegisterBody
    ): RegisterResponse


    @GET("http://34.101.115.114:5000/penyakit")
    suspend fun getDiseaseRemote(
        @Header("Authorization") token : String
    ): NewDiseaseResultRespon


    @GET("http://34.101.115.114:5000/penyakit/{id}")
    suspend fun getDiseaseRemoteById(
        @Path("id") id: Int,
        @Header("Authorization") token : String
    ): NewDiseaseResultRespon


    @Multipart
    @POST("http://34.101.115.114:5000/penyakit")
    suspend fun predictionDisease(
        @Part image : MultipartBody.Part,
        @Part("latitude") latitude : Double,
        @Part("longitude") longitude: Double,
    ): DiseasePostResponse

    @DELETE("http://34.101.115.114:5000/penyakit/{id}")
    suspend fun deleteDiseaseRemote(
        @Path("id") id: Int,
        @Header("Authorization") token : String
    ): DiseasePostResponse


}