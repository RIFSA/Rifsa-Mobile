package com.example.rifsa_mobile.model.remote

import androidx.lifecycle.LiveData
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostBody
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResultRespon
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryRespon
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

    //TODO | harvest update, delete
    @POST("hasilpanen")
    suspend fun postHarvestResult(
        @Body body: HarvestPostBody
    ): HarvestResultRespon

    @GET("hasilpanen")
    suspend fun getHarvestResult(): HarvestResultRespon


    //TODO | inventory update, delete

    @Multipart
    @POST("inventaris")
    suspend fun postInventory(
        @Part("nama") name : String,
        @Part file : MultipartBody.Part,
        @Part("jumlah") jumlah : Int,
        @Part("catatan") catatan : String
    ): InventoryRespon
}