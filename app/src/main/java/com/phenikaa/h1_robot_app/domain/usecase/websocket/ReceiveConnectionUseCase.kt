package com.phenikaa.h1_robot_app.domain.usecase.websocket

import com.phenikaa.h1_robot_app.domain.entity.Connection
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class ReceiveConnectionUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke(): Flow<Connection> {
        return webSocketRepository.receiveConnection()
    }
}