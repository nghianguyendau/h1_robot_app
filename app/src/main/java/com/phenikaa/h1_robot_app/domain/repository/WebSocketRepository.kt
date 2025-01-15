package com.phenikaa.h1_robot_app.domain.repository

import kotlinx.coroutines.flow.Flow
import com.phenikaa.h1_robot_app.data.datasource.websocket.ConnectionState

interface WebSocketRepository {
    suspend fun connect(url: String)
    suspend fun disconnect()
    suspend fun sendMessage(message: String)
    fun receiveMessages(): Flow<String>
    fun observeConnectionState(): Flow<ConnectionState>
}