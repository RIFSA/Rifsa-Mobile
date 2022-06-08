package com.example.rifsa_mobile.model.remote

import com.example.rifsa_mobile.model.entity.remote.disease.DiseasePostResponse
import com.example.rifsa_mobile.model.entity.remote.disease.DiseasePredictionResponse
import com.example.rifsa_mobile.model.entity.remote.disease.DiseaseResultResponse
import com.example.rifsa_mobile.model.entity.remote.finance.FinancePostBody
import com.example.rifsa_mobile.model.entity.remote.finance.FinancePostResponse
import com.example.rifsa_mobile.model.entity.remote.finance.FinanceResultResponse
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostBody
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestPostResponse
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResultRespon
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryPostResponse
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryResultRespon
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



    //TODO | harvest add, update, delete
    @POST("hasilpanen")
    suspend fun postHarvestResult(
        @Body body: HarvestPostBody
    ): HarvestPostResponse

    @PUT("hasilpanen/{id}")
    suspend fun updateHarvestResult(
        @Path("id") id : Int,
        @Body body: HarvestPostBody
    ): HarvestPostResponse

    @DELETE("hasilpanen/{id}")
    suspend fun deleteHarvestResult(
        @Path("id") id : Int
    ): HarvestPostResponse

    @GET("hasilpanen")
    suspend fun getHarvestResult(): HarvestResultRespon



    //TODO | finance add, update, delete
    @GET("keuangan")
    suspend fun getFinanceResult() : FinanceResultResponse


    @POST("keuangan")
    suspend fun postFinance(
        @Body body: FinancePostBody
    ): FinancePostResponse



    @DELETE("keuangan/{id}")
    suspend fun deleteFinance(
        @Path("id") id : Int
    ): FinancePostResponse

    @PUT("keuangan/{id}")
    suspend fun updateFinance(
        @Path("id") id: Int,
        @Body body: FinancePostBody
    ): FinancePostResponse


    //TODO | inventory update, delete
    @GET("inventaris")
    suspend fun getInventory(): InventoryResultRespon

    @Multipart
    @POST("inventaris")
    suspend fun postInventory(
        @Part("nama") name : String,
        @Part file : MultipartBody.Part,
        @Part("jumlah") jumlah : Int,
        @Part("catatan") catatan : String
    ): InventoryPostResponse


    @Multipart
    @PUT("inventaris/{id}")
    suspend fun updateInventory(
        @Part("nama") name : String,
        @Part file : MultipartBody.Part,
        @Part("jumlah") jumlah : Int,
        @Part("catatan") catatan : String,
        @Path("id") id : Int
    ): InventoryPostResponse


    //todo disease  post , predic , list , delete

    @GET("penyakit")
    suspend fun getDiseaseList(): DiseaseResultResponse


    @Multipart
    @POST("http://34.101.115.114:5000/predict")
    suspend fun predictionDisease(
        @Part image : MultipartBody.Part
    ): DiseasePredictionResponse


}