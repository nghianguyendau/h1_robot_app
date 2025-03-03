package com.phenikaa.h1_robot_app.data.datasource.websocket

import android.util.Log
import com.google.gson.Gson
import com.phenikaa.h1_robot_app.data.datasource.websocket.base.WebSocketClient
import com.phenikaa.h1_robot_app.data.model.RobotRouteData
import com.phenikaa.h1_robot_app.domain.entity.RobotRoute
import com.phenikaa.h1_robot_app.shared.constants.EnvConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppWebSocketService(
    private val webSocketClient: WebSocketClient,
    private val gson: Gson
) {

    fun connectRobotRoute() {
        webSocketClient.connect(EnvConstants.ROUTE_WEBSOCKET_URL)
    }

    fun disconnectRobotRoute() {
        webSocketClient.disconnect()
    }

    fun sendMessageRobotRoute(event: String, pointId: List<Int>) {
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

}