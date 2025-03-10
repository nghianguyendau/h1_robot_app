package com.phenikaa.h1_robot_app.domain.usecase.websocket

import com.phenikaa.h1_robot_app.data.model.HoldDoorOpenRequest
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository

class SendHoldDoorOpenRequestUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke(request: HoldDoorOpenRequest) {
        webSocketRepository.sendHoldDoorOpenRequest(request)
    }
}