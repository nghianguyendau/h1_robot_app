package com.phenikaa.h1_robot_app.domain.repository

import kotlinx.coroutines.flow.Flow
import com.phenikaa.h1_robot_app.data.source.websocket.ConnectionState
import com.phenikaa.h1_robot_app.domain.entity.RobotRoute
import com.phenikaa.h1_robot_app.domain.entity.RobotRoutePose

interface WebSocketRepository {
    suspend fun connect(url: String)
    suspend fun disconnect()
    suspend fun sendMessage(message: String)
    fun receiveMessages(): Flow<String>
    fun observeConnectionState(): Flow<ConnectionState>
    suspend fun connectRobotRoute()
    suspend fun disconnectRobotRoute()
    suspend fun sendMessageRobotRouteAnalyze(event: String, pointId: List<Int>)
    suspend fun receiveMessagesRobotRoute(): RobotRoute
    suspend fun robotRouteNaviPos(robotRoutePose: RobotRoutePose)
    suspend fun sendMessageRobotRouteCancelTask(event: String)
    suspend fun sendMessageRobotRouteTaskStepConfirm(event: String,data: String)
    suspend fun sendMessageRobotRouteTaskStageFinish(event: String, stageId: Int, status: Int)
}