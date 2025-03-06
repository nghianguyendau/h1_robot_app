package com.phenikaa.h1_robot_app.data.repository

import android.util.Log
import com.google.gson.Gson
import com.phenikaa.h1_robot_app.data.api.ApiClient
import com.phenikaa.h1_robot_app.data.source.websocket.BaseWebSocketDataSource
import com.phenikaa.h1_robot_app.data.source.websocket.ConnectionState
import com.phenikaa.h1_robot_app.data.model.ElevatorRequest
import com.phenikaa.h1_robot_app.data.model.ElevatorResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ElevatorRepository @Inject constructor(
    private val baseWebSocketDataSource: BaseWebSocketDataSource,
    private val gson: Gson,
) {

    suspend fun callElevator(currentFloor: Int, destinationFloor: Int): ElevatorResponse {
        // Tạo instance ElevatorRequest
        val request = ElevatorRequest(
            current_floor = currentFloor,
            destination_floor = destinationFloor
        )
//        val response =ApiClient.apiService.callElevator(request, "RobotSN01")

        return ApiClient.apiService.callElevator(request, "RobotSN01")
    }
    fun connect() {
//        baseWebSocketDataSource.connect("wss://robotic-phenikaa-mec-server.phx.asia?type=ROBOT&serial_number=RobotSN01")
        baseWebSocketDataSource.connect("ws://271e-118-70-209-177.ngrok-free.app?type=ROBOT&serial_number=SN01")
    }

    fun disconnect() {
        baseWebSocketDataSource.disconnect()
    }

    fun sendMessage(message: String) {
        if(baseWebSocketDataSource.connectionState.value == ConnectionState.CONNECTED) {
            baseWebSocketDataSource.sendMessage(message)
        }
        else {
            Log.e("Websocket", "DISCONNECTED")
        }
    }

    fun receiveMessages(): Flow<String> {
        return baseWebSocketDataSource.receiveMessages()
    }

    fun getConnectionState(): StateFlow<ConnectionState> {
        return baseWebSocketDataSource.connectionState
    }
}