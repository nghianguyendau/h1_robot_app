package com.phenikaa.h1_robot_app.domain.repository

import com.phenikaa.h1_robot_app.data.model.CancelHoldDoorRequest
import com.phenikaa.h1_robot_app.data.model.HoldDoorOpenRequest
import com.phenikaa.h1_robot_app.data.model.MakeElevatorCallRequest
import com.phenikaa.h1_robot_app.data.model.SiteMonitoringRequest
import kotlinx.coroutines.flow.Flow
import com.phenikaa.h1_robot_app.data.source.websocket.ConnectionState
import com.phenikaa.h1_robot_app.domain.entity.Connection
import com.phenikaa.h1_robot_app.domain.entity.State
import com.phenikaa.h1_robot_app.domain.entity.RobotRoute
import com.phenikaa.h1_robot_app.domain.entity.RobotRoutePose
import com.phenikaa.h1_robot_app.domain.entity.SiteMonitoring

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

    // KONE WS
    suspend fun connectKoneElevator(accessToken: String)
    fun sendSiteMonitoringRequest(request: SiteMonitoringRequest)
    fun receiveSiteMonitoringResponse(): Flow<SiteMonitoring>
    fun sendMakeElevatorCallRequest(request: MakeElevatorCallRequest)
    fun sendHoldDoorOpenRequest(request: HoldDoorOpenRequest)
    fun sendCancelHoldDoorRequest(request: CancelHoldDoorRequest)

    // Response Connection Hold Door = Response Connection Make Elevator Call
    fun receiveConnection(): Flow<Connection>
    // Response State Hold Door = Response State Make Elevator Call = Response State Cancel Hold Door
    fun receiveState(): Flow<State>
}