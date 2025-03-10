package com.phenikaa.h1_robot_app.data.source.websocket

import com.google.gson.Gson
import com.phenikaa.h1_robot_app.data.mapper.SiteMonitoringResponseMapper
import com.phenikaa.h1_robot_app.data.model.CancelHoldDoorRequest
import com.phenikaa.h1_robot_app.data.model.HoldDoorOpenRequest
import com.phenikaa.h1_robot_app.data.model.MakeElevatorCallRequest
import com.phenikaa.h1_robot_app.data.model.ResponseConnection
import com.phenikaa.h1_robot_app.data.model.ResponseState
import com.phenikaa.h1_robot_app.data.source.websocket.base.WebSocketClient
import com.phenikaa.h1_robot_app.data.model.RobotRouteData
import com.phenikaa.h1_robot_app.data.model.SiteMonitoringRequest
import com.phenikaa.h1_robot_app.data.model.SiteMonitoringResponse
import com.phenikaa.h1_robot_app.shared.constants.EnvConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppWebSocketService(
    private val webSocketClient: WebSocketClient,
    private val gson: Gson,
    private val monitoringResponseMapper: SiteMonitoringResponseMapper
) {

    fun connectRobotRoute() {
        webSocketClient.connect(EnvConstants.ROUTE_WEBSOCKET_URL)
    }

    fun connectKoneElevator(accessToken: String) {
        webSocketClient.connect(EnvConstants.KONE_ELEVATOR_WEBSOCKET_URL + accessToken)
    }

    fun disconnectRobotRoute() {
        webSocketClient.disconnect()
    }

    fun sendMessageRobotRouteAnalyze(event: String, pointId: List<Int>) {
        val data = mapOf(
            "event" to event,
            "data" to pointId
        )
        val jsonData = Gson().toJson(data)
        webSocketClient.sendMessage(jsonData)
    }

    fun receiveMessagesRobotRoute(): Flow<RobotRouteData> {
        return webSocketClient.receiveMessages().map { messenger ->
            gson.fromJson(messenger, RobotRouteData::class.java)
        }
    }

    fun sendMessageRobotRouteCancelTask(event: String) {
        val data = mapOf(
            "event" to event,
        )
        val jsonData = Gson().toJson(data)
        webSocketClient.sendMessage(jsonData)
    }

    fun sendMessageRobotRouteTaskStepConfirm(event: String, data: String) {
        val input = mapOf(
            "event" to event,
            "data" to data
        )
        val jsonData = Gson().toJson(input)
        webSocketClient.sendMessage(jsonData)
    }

    fun sendMessageRobotRouteTaskStageFinish(event: String, stageId: Int, status: Int) {
        val data = mapOf(
            "stage_id" to stageId,
            "status" to status
        )
        val input = mapOf(
            "event" to event,
            "data" to data
        )
        val jsonData = Gson().toJson(input)
        webSocketClient.sendMessage(jsonData)
    }

    fun sendMessageSiteMonitoring(request: SiteMonitoringRequest) {
        val jsonData = gson.toJson(request)
        webSocketClient.sendMessage(jsonData)
    }

    fun receiveMonitoringMessages(): Flow<SiteMonitoringResponse> {
        return webSocketClient.receiveMessages().map { message ->
            gson.fromJson(message, SiteMonitoringResponse::class.java)
        }
    }

    fun sendMakeElevatorCallRequest(request: MakeElevatorCallRequest) {
        val jsonData = gson.toJson(request)
        webSocketClient.sendMessage(jsonData)
    }

    fun sendHoldDoorOpenRequest(request: HoldDoorOpenRequest) {
        val jsonData = gson.toJson(request)
        webSocketClient.sendMessage(jsonData)
    }

    fun sendCancelHoldDoorRequest(request: CancelHoldDoorRequest) {
        val jsonData = gson.toJson(request)
        webSocketClient.sendMessage(jsonData)
    }

    fun receiveConnection(): Flow<ResponseConnection> {
        return webSocketClient.receiveMessages().map { messenger ->
            gson.fromJson(messenger, ResponseConnection::class.java)
        }
    }

    fun receiveState(): Flow<ResponseState> {
        return webSocketClient.receiveMessages().map { messenger ->
            gson.fromJson(messenger, ResponseState::class.java)
        }
    }
}