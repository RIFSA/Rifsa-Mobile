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
import java.util.*

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
    @GET("hasilpanen")
    suspend fun getHarvestResultRemote(): HarvestResultRespon

    @POST("hasilpanen")
    suspend fun postHarvestResultRemote(
        @Body body: HarvestPostBody
    ): HarvestPostResponse

    @PUT("hasilpanen/{id}")
    suspend fun updateHarvestResultRemote(
        @Path("id") id : Int,
        @Body body: HarvestPostBody
    ): HarvestPostResponse

    @DELETE("hasilpanen/{id}")
    suspend fun deleteHarvestResultRemote(
        @Path("id") id : Int
    ): HarvestPostResponse





    //TODO | finance add, update, delete
    @GET("keuangan")
    suspend fun getFinanceRemote() : FinanceResultResponse

    @POST("keuangan")
    suspend fun postFinanceRemote(
        @Body body: FinancePostBody
    ): FinancePostResponse

    @DELETE("keuangan/{id}")
    suspend fun deleteFinanceRemote(
        @Path("id") id : Int
    ): FinancePostResponse

    @PUT("keuangan/{id}")
    suspend fun updateFinanceRemote(
        @Path("id") id: Int,
        @Body body: FinancePostBody
    ): FinancePostResponse


    //TODO | inventory update, delete
    @GET("inventaris")
    suspend fun getInventoryRemote(): InventoryResultRespon

    @Multipart
    @POST("inventaris")
    suspend fun postInventoryRemote(
        @Part("nama") name : String,
        @Part file : MultipartBody.Part,
        @Part("jumlah") jumlah : Int,
        @Part("catatan") catatan : String
    ): InventoryPostResponse


    @Multipart
    @PUT("inventaris/{id}")
    suspend fun updateInventoryRemote(
        @Part("nama") name : String,
        @Part file : MultipartBody.Part,
        @Part("jumlah") jumlah : Int,
        @Part("catatan") catatan : String,
        @Path("id") id : Int
    ): InventoryPostResponse


    //todo disease  post , predic , list , delete

    @GET("penyakit")
    suspend fun getDiseaseRemote(): DiseaseResultResponse


    @Multipart
    @POST("http://34.101.115.114:5000/predict")
    suspend fun predictionDisease(
        @Part image : MultipartBody.Part
    ): DiseasePredictionResponse

    @Multipart
    @POST("http://34.101.50.17:5000/penyakit")
    suspend fun postDiseaseRemote(
        @Part("nama") nama : String,
        @Part file : MultipartBody.Part,
        @Part("indikasi") indikasi : String,
        @Part("deskripsi") deskripsi : String,
        @Part("latitude") latitude : Double,
        @Part("longitude") longitude : Double
    ): DiseasePostResponse

}