package com.phenikaa.h1_robot_app.domain.usecase.robot_route

import com.phenikaa.h1_robot_app.domain.entity.RobotRoute
import com.phenikaa.h1_robot_app.domain.entity.RobotRoutePose
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class robotRouteNaviPosUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    suspend operator fun invoke(robotRoutePose: RobotRoutePose) = webSocketRepository.robotRouteNaviPos(robotRoutePose)
}