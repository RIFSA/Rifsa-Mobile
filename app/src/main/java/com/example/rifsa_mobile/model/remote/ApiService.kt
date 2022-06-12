package com.example.rifsa_mobile.model.remote

import com.example.rifsa_mobile.model.entity.remote.disease.DiseasePostResponse
import com.example.rifsa_mobile.model.entity.remote.disease.DiseasePredictionResponse
import com.example.rifsa_mobile.model.entity.remote.disease.DiseaseResultResponse
import com.example.rifsa_mobile.model.entity.remote.disease.NewDiseasePostRespon
import com.example.rifsa_mobile.model.entity.remote.disease.restapivm.NewDiseaseResultRespon
import com.example.rifsa_mobile.model.entity.remote.disease.restapivm.NewDiseaseResultResponItem
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

    @GET("hasilpanen")
    suspend fun getHarvestResultRemote(
        @Header("Authorization") token : String
    ): HarvestResultRespon

    @POST("hasilpanen")
    suspend fun postHarvestResultRemote(
        @Body body: HarvestPostBody,
        @Header("Authorization") token : String
    ): HarvestPostResponse

    @PUT("hasilpanen/{id}")
    suspend fun updateHarvestResultRemote(
        @Path("id") id : Int,
        @Body body: HarvestPostBody,
        @Header("Authorization") token : String
    ): HarvestPostResponse

    @DELETE("hasilpanen/{id}")
    suspend fun deleteHarvestResultRemote(
        @Path("id") id : Int,
        @Header("Authorization") token : String
    ): HarvestPostResponse


    @GET("keuangan")
    suspend fun getFinanceRemote(
        @Header("Authorization") token : String
    ) : FinanceResultResponse

    @POST("keuangan")
    suspend fun postFinanceRemote(
        @Body body: FinancePostBody,
        @Header("Authorization") token : String
    ): FinancePostResponse

    @DELETE("keuangan/{id}")
    suspend fun deleteFinanceRemote(
        @Path("id") id : Int,
        @Header("Authorization") token : String
    ): FinancePostResponse

    @PUT("keuangan/{id}")
    suspend fun updateFinanceRemote(
        @Path("id") id: Int,
        @Body body: FinancePostBody,
        @Header("Authorization") token : String
    ): FinancePostResponse


    @GET("inventaris")
    suspend fun getInventoryRemote(
        @Header("Authorization") token : String
    ): InventoryResultRespon

    @Multipart
    @POST("inventaris")
    suspend fun postInventoryRemote(
        @Part("nama") name : String,
        @Part file : MultipartBody.Part,
        @Part("jumlah") jumlah : Int,
        @Part("catatan") catatan : String,
        @Header("Authorization") token : String
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

    @DELETE("inventaris/{id}")
    suspend fun deleteInventoryRemote(
        @Path("id") id : Int,
        @Header("Authorization") token : String
    ): InventoryPostResponse




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
        @Part("nama") nama : String,
        @Part("tanggal") tanggal : String,
        @Part("deskripsi") deskripsi: String,
        @Header("Authorization") token : String
    ): NewDiseasePostRespon



    //TODO | tidak dipakai
    @Multipart
    @POST("penyakit")
    suspend fun postDiseaseRemote(
        @Part("nama") nama : String,
        @Part file : MultipartBody.Part,
        @Part("indikasi") indikasi : String,
        @Part("deskripsi") deskripsi : String,
        @Part("latitude") latitude : Double,
        @Part("longitude") longitude : Double,
        @Header("Authorization") token : String
    ): DiseasePostResponse


    @DELETE("http://34.101.115.114:5000/penyakit/{id}")
    suspend fun deleteDiseaseRemote(
        @Path("id") id: Int,
        @Header("Authorization") token : String
    ): DiseasePostResponse

}