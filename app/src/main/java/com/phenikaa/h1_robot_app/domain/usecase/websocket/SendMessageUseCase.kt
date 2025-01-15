package com.phenikaa.h1_robot_app.domain.usecase.websocket

import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: WebSocketRepository
) {
    suspend operator fun invoke(message: String) {
        repository.sendMessage(message)
    }
}