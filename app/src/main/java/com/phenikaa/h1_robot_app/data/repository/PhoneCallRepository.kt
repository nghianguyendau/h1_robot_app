package com.phenikaa.h1_robot_app.data.repository

import com.google.gson.Gson
import com.phenikaa.h1_robot_app.data.datasource.websocket.BaseWebSocketDataSource
import com.phenikaa.h1_robot_app.data.datasource.websocket.ConnectionState
import com.phenikaa.h1_robot_app.domain.model.PhoneCallMessage
import com.phenikaa.h1_robot_app.domain.model.PhoneCallResponseMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhoneCallRepository @Inject constructor(
    private val baseWebSocketDataSource: BaseWebSocketDataSource,
    private val gson: Gson
) {
    fun connect(url: String) {
        baseWebSocketDataSource.connect(url)
    }

    fun disconnect() {
        baseWebSocketDataSource.disconnect()
    }

    fun sendPhoneCallMessage(message: PhoneCallMessage) {
        val json = gson.toJson(message)
        baseWebSocketDataSource.sendMessage(json)
    }

    fun receivePhoneCallMessages(): Flow<PhoneCallResponseMessage> {
        return baseWebSocketDataSource.receiveMessages()
            .map { json -> gson.fromJson(json, PhoneCallResponseMessage::class.java) }
    }

    fun observeConnectionState(): Flow<ConnectionState> {
        return baseWebSocketDataSource.connectionState
    }
}