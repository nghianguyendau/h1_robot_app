package com.phenikaa.h1_robot_app.domain.usecase.robot_route

import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import com.phenikaa.h1_robot_app.shared.constants.AppConstants

class RobotRouteSendMessengerTaskStepConfirmUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    suspend operator fun invoke(confirmCode: String) =
        webSocketRepository.sendMessageRobotRouteTaskStepConfirm(
            AppConstants.TASK_STEP_CONFIRMED,
            confirmCode
        )
}