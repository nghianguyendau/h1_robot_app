package com.phenikaa.h1_robot_app.domain.usecase.robot_route

import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import com.phenikaa.h1_robot_app.shared.constants.AppConstants

class RobotRouteSendMessengerCancelTaskUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    suspend operator fun invoke() =
        webSocketRepository.sendMessageRobotRouteCancelTask(AppConstants.CANCEL_TASK)
}