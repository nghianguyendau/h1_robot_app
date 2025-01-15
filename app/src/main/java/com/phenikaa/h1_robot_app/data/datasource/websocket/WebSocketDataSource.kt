package com.phenikaa.h1_robot_app.data.datasource.websocket

import android.util.Log
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.*
import com.google.gson.Gson
import javax.inject.Inject

class BaseWebSocketDataSource @Inject constructor(
    private val gson: Gson,
    private val client: OkHttpClient
) {
    private var webSocket: WebSocket? = null
    private val messageChannel = Channel<String>()
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _connectionState.value = ConnectionState.CONNECTED
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                if (!messageChannel.trySend(text).isSuccess) {
                    Log.e("WebSocket", "Failed to send message to channel")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _connectionState.value = ConnectionState.DISCONNECTED
                t.printStackTrace()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _connectionState.value = ConnectionState.DISCONNECTED
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "Disconnected")
        webSocket = null
        _connectionState.value = ConnectionState.DISCONNECTED
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun receiveMessages(): Flow<String> = messageChannel.receiveAsFlow()
}

enum class ConnectionState {
    CONNECTED,
    DISCONNECTED
}