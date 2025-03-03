package com.phenikaa.h1_robot_app.domain.usecase.robot_route

import com.phenikaa.h1_robot_app.domain.entity.RobotRoute
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository

class RobotRouteReceiveMessagesUseCase (
    private val webSocketRepository: WebSocketRepository
) {
    suspend operator fun invoke(): RobotRoute = webSocketRepository.receiveMessagesRobotRoute()
}