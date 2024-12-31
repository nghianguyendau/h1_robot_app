package com.phenikaa.h1_robot_app.domain.usecase.websocket

import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import javax.inject.Inject

class ReceiveMessagesUseCase @Inject constructor(
    private val repository: WebSocketRepository
) {
    fun invoke() = repository.receiveMessages()
}
