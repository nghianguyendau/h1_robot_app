package com.phenikaa.h1_robot_app.domain.usecase.robot_route

import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import com.phenikaa.h1_robot_app.shared.constants.AppConstants

class RobotRouteTaskStageFinishUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    suspend operator fun invoke(stageId: Int, status: Int) =
        webSocketRepository.sendMessageRobotRouteTaskStageFinish(
            AppConstants.STAGE_FINISH, stageId, status
        )
}