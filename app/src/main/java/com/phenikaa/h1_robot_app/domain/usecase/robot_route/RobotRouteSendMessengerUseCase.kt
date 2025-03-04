package com.phenikaa.h1_robot_app.domain.usecase.robot_route

import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository

class RobotRouteSendMessengerRouteAnalyzeUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    suspend operator fun invoke(event: String, pointId: List<Int>) =
        webSocketRepository.sendMessageRobotRouteAnalyze(event, pointId)
}