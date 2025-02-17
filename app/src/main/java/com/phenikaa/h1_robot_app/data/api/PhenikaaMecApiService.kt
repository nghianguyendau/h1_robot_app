package com.phenikaa.h1_robot_app.data.api

import com.phenikaa.h1_robot_app.data.model.ModelFloorApi
import com.phenikaa.h1_robot_app.data.model.ModelPointApi
import com.phenikaa.h1_robot_app.data.model.ModelPointsByFloorId
import com.phenikaa.h1_robot_app.data.model.NewPoint
import retrofit2.Response
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface PhenikaaMecApiService {
    // points
    @GET("points")
    suspend fun getPoints(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ModelPointApi

    @GET("points/{floor_id}")
    suspend fun getPointsByFloorId(@Path("floor_id") floorId: Int): ModelPointsByFloorId

    @POST("points")
    suspend fun savePoint(@Body point: NewPoint): Response<Unit>

    @PUT("points/{id}")
    suspend fun updatePoint(@Path("id") id: Int, @Body point: NewPoint): Response<Unit>

    @DELETE("points/{id}")
    suspend fun deletePoint(@Path("id") id: Int): Response<Unit>

    // floors
    @GET("floors") // get all floors
    suspend fun getAllFloors(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ModelFloorApi

}

object PhenikaaMecApiClient {
    private const val BASE_URL = "https://38d0-2a09-bac5-d45a-16dc-00-247-128.ngrok-free.app/"

    val apiService: PhenikaaMecApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhenikaaMecApiService::class.java)
    }
}
