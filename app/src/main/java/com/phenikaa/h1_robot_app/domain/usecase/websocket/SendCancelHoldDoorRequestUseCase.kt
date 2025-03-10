package com.phenikaa.h1_robot_app.domain.usecase.websocket

import com.phenikaa.h1_robot_app.data.model.CancelHoldDoorRequest
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository

class SendCancelHoldDoorRequestUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke(request: CancelHoldDoorRequest) {
        webSocketRepository.sendCancelHoldDoorRequest(request)
    }
}