package com.anandamartiza0128.makanapaya.network

import com.anandamartiza0128.makanapaya.model.Makanan
import com.anandamartiza0128.makanapaya.model.MakananApiResponse
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

private const val BASE_URL = "https://makanapaya.sendiko.my.id/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MakananApiService {
    @GET("api/makanans")
    suspend fun getMakananList(
        @Header("Authorization") authHeader: String,
        @Header("user_id") userId: String
    ): MakananApiResponse

    @Multipart
    @POST("api/makanans")
    suspend fun postMakanan(
        @Header("Authorization") authHeader: String,
        @Header("user_id") userId: String,
        @Part("nama") nama: RequestBody,
        @Part("jenis") jenis: RequestBody,
        @Part("rasa") rasa: RequestBody,
        @Part("tingkatPedas") tingkatPedas: RequestBody,
        @Part("tekstur") tekstur: RequestBody,
        @Part image: MultipartBody.Part?
    ): Makanan

    @DELETE("api/makanans/{id}")
    suspend fun deleteMakanan(
        @Path("id") id: Int,
        @Header("Authorization") authHeader: String,
        @Header("user_id") userId: String
    ): Response<ResponseBody>

    @Multipart
    @PUT("api/makanans/{id}")
    suspend fun updateMakanan(
        @Path("id") id: Int,
        @Header("Authorization") authHeader: String,
        @Header("user_id") userId: String,
        @Part("nama") nama: RequestBody,
        @Part("jenis") jenis: RequestBody,
        @Part("rasa") rasa: RequestBody,
        @Part("tingkatPedas") tingkatPedas: RequestBody,
        @Part("tekstur") tekstur: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Makanan>
}

object MakananApi {
    val service: MakananApiService by lazy {
        retrofit.create(MakananApiService::class.java)
    }

    fun getMakananImageUrl(imageUri: String): String {
        return "$BASE_URL$imageUri"
    }
}