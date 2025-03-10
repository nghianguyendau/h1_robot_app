package com.phenikaa.h1_robot_app.domain.usecase.websocket

import com.phenikaa.h1_robot_app.data.model.MakeElevatorCallRequest
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository

class SendMakeElevatorCallRequestUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke(request: MakeElevatorCallRequest) {
        webSocketRepository.sendMakeElevatorCallRequest(request)
    }
}