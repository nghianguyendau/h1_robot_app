package com.phenikaa.h1_robot_app.data.repository

import android.util.Log
import com.phenikaa.h1_robot_app.data.datasource.websocket.BaseWebSocketDataSource
import com.phenikaa.h1_robot_app.data.datasource.websocket.ConnectionState
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebSocketRepositoryImpl @Inject constructor(
    private val dataSource: BaseWebSocketDataSource
) : WebSocketRepository {
    override suspend fun connect(url: String) {
        dataSource.connect(url)
    }

    override suspend fun disconnect() {
        dataSource.disconnect()
    }

    override suspend fun sendMessage(message: String) {
        dataSource.sendMessage(message)
        Log.d("WebSocketRepositoryImpl", "Sent message: $message")
    }

    override fun receiveMessages(): Flow<String> {
        return dataSource.receiveMessages()
    }

    override fun observeConnectionState(): Flow<ConnectionState> {
        return dataSource.connectionState
    }
}
