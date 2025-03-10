package com.phenikaa.h1_robot_app.domain.usecase.websocket

import com.phenikaa.h1_robot_app.domain.entity.State
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class ReceiveStateUseCase(
    private val webSocketRepository: WebSocketRepository
) {
    operator fun invoke(): Flow<State> {
        return webSocketRepository.receiveState()
    }
}