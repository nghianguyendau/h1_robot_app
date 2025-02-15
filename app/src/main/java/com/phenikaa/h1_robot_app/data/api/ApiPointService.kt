package com.phenikaa.h1_robot_app.data.api

import com.phenikaa.h1_robot_app.data.model.ModelPointApi
import com.phenikaa.h1_robot_app.data.model.NewPoint
import com.phenikaa.h1_robot_app.data.model.Point
import com.phenikaa.h1_robot_app.data.model.RosPosition
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface PointsApiService {
    @GET("points")
    suspend fun getPoints(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ModelPointApi

    @POST("points")
    suspend fun savePoint(@Body point: NewPoint): Response<Unit>

    @PUT("points/{id}")
    suspend fun updatePoint(@Path("id") id: Int, @Body point: NewPoint): Response<Unit>

    @DELETE("points/{id}")
    suspend fun deletePoint(@Path("id") id: Int): Response<Unit>
}

object ApiPointClient {
    private const val BASE_URL = "https://robotic-phenikaa-mec-server.phx.asia/"

    val apiService: PointsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PointsApiService::class.java)
    }
}
