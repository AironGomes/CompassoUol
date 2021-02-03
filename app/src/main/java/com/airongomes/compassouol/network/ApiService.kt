package com.airongomes.compassouol.network

import com.bumptech.glide.request.BaseRequestOptions
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.addAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


private const val BASE_URL = "https://5f5a8f24d44d640016169133.mockapi.io/"

// Moshi é usado na conversão Json / Kotlin Objetos
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory()) // to use Moshi annotation with Kotlin
    .build()


// Builder do Retrofit
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()


/**
 * Interface para operações de GET e POST
 */
interface ApiService {
    @GET("api/events") //endpoint
    suspend fun getProperties(): MutableList<EventProperty>

    @GET("api/events/{id}") //endpoint with id
    suspend fun getPropertiesId(@Path("id") id: Int): EventProperty

    @POST("api/checkin")
    suspend fun registerUser(@Body userInfo: UserInfo): Response<ResponseBody>
}

/**
 * Objeto responsável por criar o retrofit
 */
object Api {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}