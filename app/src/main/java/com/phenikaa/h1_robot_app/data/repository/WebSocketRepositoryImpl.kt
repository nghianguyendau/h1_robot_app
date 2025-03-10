package com.phenikaa.h1_robot_app.data.repository

import android.util.Log
import com.phenikaa.h1_robot_app.data.mapper.ResponseConnectionMapper
import com.phenikaa.h1_robot_app.data.mapper.ResponseStateMapper
import com.phenikaa.h1_robot_app.data.source.robot.RobotNaviDataSource
import com.phenikaa.h1_robot_app.data.source.websocket.AppWebSocketService
import com.phenikaa.h1_robot_app.data.source.websocket.BaseWebSocketDataSource
import com.phenikaa.h1_robot_app.data.source.websocket.ConnectionState
import com.phenikaa.h1_robot_app.data.mapper.RobotRouteMapper
import com.phenikaa.h1_robot_app.data.mapper.RobotRoutePoseMapper
import com.phenikaa.h1_robot_app.data.mapper.SiteMonitoringResponseMapper
import com.phenikaa.h1_robot_app.data.model.CancelHoldDoorRequest
import com.phenikaa.h1_robot_app.data.model.HoldDoorOpenRequest
import com.phenikaa.h1_robot_app.data.model.MakeElevatorCallRequest
import com.phenikaa.h1_robot_app.data.model.SiteMonitoringRequest
import com.phenikaa.h1_robot_app.domain.entity.Connection
import com.phenikaa.h1_robot_app.domain.entity.State
import com.phenikaa.h1_robot_app.domain.entity.RobotRoute
import com.phenikaa.h1_robot_app.domain.entity.RobotRoutePose
import com.phenikaa.h1_robot_app.domain.entity.SiteMonitoring
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
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
    private val siteMonitoringResponseMapper: SiteMonitoringResponseMapper,
    private val makeElevatorCallResponseConnectionMapper: ResponseConnectionMapper,
    private val makeElevatorCallResponseStateMapper: ResponseStateMapper
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

    override suspend fun sendMessageRobotRouteAnalyze(event: String, pointId: List<Int>) {
        appWebSocketService.sendMessageRobotRouteAnalyze(event, pointId)
    }

    override suspend fun receiveMessagesRobotRoute(): RobotRoute {
        return appWebSocketService.receiveMessagesRobotRoute().map { messenger ->
            robotRouteMapper.mapToEntity(messenger)
        }.first()
    }

    override suspend fun robotRouteNaviPos(robotRoutePose: RobotRoutePose) {
        return robotNaviDataSource.robotRouteNaviPos(robotRoutePoseMapper.mapToData(robotRoutePose))
    }

    override suspend fun sendMessageRobotRouteCancelTask(event: String) {
        appWebSocketService.sendMessageRobotRouteCancelTask(event)
    }

    override suspend fun sendMessageRobotRouteTaskStepConfirm(event: String, data: String) {
        appWebSocketService.sendMessageRobotRouteTaskStepConfirm(event, data)
    }

    override suspend fun sendMessageRobotRouteTaskStageFinish(
        event: String,
        stageId: Int,
        status: Int
    ) {
        appWebSocketService.sendMessageRobotRouteTaskStageFinish(event, stageId, status)
    }


    // KONE WS
    override suspend fun connectKoneElevator(accessToken: String) {
        appWebSocketService.connectKoneElevator(accessToken)
    }

    override fun sendSiteMonitoringRequest(request: SiteMonitoringRequest) {
        appWebSocketService.sendMessageSiteMonitoring(request)
    }

    override fun receiveSiteMonitoringResponse(): Flow<SiteMonitoring> {
        return appWebSocketService.receiveMonitoringMessages().map { message ->
            siteMonitoringResponseMapper.mapToEntity(message)
        }
    }

    override fun sendMakeElevatorCallRequest(request: MakeElevatorCallRequest) {
        appWebSocketService.sendMakeElevatorCallRequest(request)
    }

    override fun sendHoldDoorOpenRequest(request: HoldDoorOpenRequest) {
        appWebSocketService.sendHoldDoorOpenRequest(request)
    }

    override fun sendCancelHoldDoorRequest(request: CancelHoldDoorRequest) {
        appWebSocketService.sendCancelHoldDoorRequest(request)
    }

    override fun receiveConnection(): Flow<Connection> {
        return appWebSocketService.receiveConnection().map { message ->
            makeElevatorCallResponseConnectionMapper.mapToEntity(message)
        }
    }

    override fun receiveState(): Flow<State> {
        return appWebSocketService.receiveState().map { message ->
            makeElevatorCallResponseStateMapper.mapToEntity(message)
        }
    }
}
