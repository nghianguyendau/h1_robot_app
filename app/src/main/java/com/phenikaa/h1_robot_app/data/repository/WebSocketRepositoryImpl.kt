package com.phenikaa.h1_robot_app.data.repository

import android.util.Log
import com.google.gson.Gson
import com.phenikaa.h1_robot_app.data.datasource.robot.RobotNaviDataSource
import com.phenikaa.h1_robot_app.data.datasource.websocket.AppWebSocketService
import com.phenikaa.h1_robot_app.data.datasource.websocket.BaseWebSocketDataSource
import com.phenikaa.h1_robot_app.data.datasource.websocket.ConnectionState
import com.phenikaa.h1_robot_app.data.datasource.websocket.base.WebSocketClient
import com.phenikaa.h1_robot_app.data.mapper.RobotRouteMapper
import com.phenikaa.h1_robot_app.data.mapper.RobotRoutePoseMapper
import com.phenikaa.h1_robot_app.data.model.RobotRouteData
import com.phenikaa.h1_robot_app.domain.entity.RobotRoute
import com.phenikaa.h1_robot_app.domain.entity.RobotRoutePose
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import com.phenikaa.h1_robot_app.shared.constants.EnvConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WebSocketRepositoryImpl @Inject constructor(
    private val dataSource: BaseWebSocketDataSource,
    private val appWebSocketService: AppWebSocketService,
    private val robotRouteMapper: RobotRouteMapper,
    private val robotNaviDataSource: RobotNaviDataSource,
    private val robotRoutePoseMapper: RobotRoutePoseMapper,
) : WebSocketRepository {
    override suspend fun connect(url: String) {
        dataSource.connect(url)
    }

    override suspend fun disconnect() {
        dataSource.disconnect()
    }

    override suspend fun sendMessage(message: String) {
        dataSource.sendMessage(message)
        Log.d("WebSocketRepositoryImpl", "Sent message: $message")
    }

    override fun receiveMessages(): Flow<String> {
        return dataSource.receiveMessages()
    }

    override fun observeConnectionState(): Flow<ConnectionState> {
        return dataSource.connectionState
    }

    override suspend fun connectRobotRoute() {
        appWebSocketService.connectRobotRoute()
    }

    override suspend fun disconnectRobotRoute() {
        appWebSocketService.disconnectRobotRoute()
    }

    override suspend fun sendMessageRobotRoute(event: String, pointId: List<Int>) {
        appWebSocketService.sendMessageRobotRoute(event,pointId)
    }

    override suspend fun receiveMessagesRobotRoute(): RobotRoute {
        return appWebSocketService.receiveMessagesRobotRoute().map { messenger ->
            robotRouteMapper.mapToEntity(messenger)
        }.first()
    }

    override suspend fun robotRouteNaviPos(robotRoutePose: RobotRoutePose) {
        return robotNaviDataSource.robotRouteNaviPos(robotRoutePoseMapper.mapToData(robotRoutePose))
    }
}
