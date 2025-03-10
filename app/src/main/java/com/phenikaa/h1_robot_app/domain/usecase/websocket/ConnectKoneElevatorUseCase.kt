package com.phenikaa.h1_robot_app.domain.usecase.websocket

import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository

class ConnectKoneElevatorUseCase(
    private val webSocketRepository: WebSocketRepository
) {
     suspend operator fun invoke(accessToken: String) = webSocketRepository.connectKoneElevator(accessToken)
}