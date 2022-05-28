package com.example.rifsa_mobile.model.remote

import androidx.room.RawQuery
import com.example.rifsa_mobile.model.remote.response.login.LoginBody
import com.example.rifsa_mobile.model.remote.response.login.LoginResponse
import com.example.rifsa_mobile.model.remote.response.signup.RegisterBody
import com.example.rifsa_mobile.model.remote.response.signup.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun postLogin(
        @Body body: LoginBody
    ): LoginResponse

    @POST("register")
    suspend fun postRegister(
        @Body body : RegisterBody
    ): RegisterResponse

}