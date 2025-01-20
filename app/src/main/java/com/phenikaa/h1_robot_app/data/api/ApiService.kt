package com.phenikaa.h1_robot_app.data.api

import com.phenikaa.h1_robot_app.data.model.ElevatorRequest
import com.phenikaa.h1_robot_app.data.model.ElevatorResponse
import com.phenikaa.h1_robot_app.data.model.ElevatorTaskStatus
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("robot/call-elevator")
    suspend fun callElevator(
        @Body request: ElevatorRequest,
        @Header("seri") robotSeri: String
    ): ElevatorResponse

//    @GET("robot/task-status/{taskId}")
//    suspend fun getElevatorTaskStatus(
//        @Path("taskId") taskId: Int
//    ): ElevatorTaskStatus
}

object ApiClient{
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://robotic-elevator-api.phx.asia/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
