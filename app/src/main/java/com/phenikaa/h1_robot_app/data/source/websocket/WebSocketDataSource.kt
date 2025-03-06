package com.phenikaa.h1_robot_app.data.source.websocket

import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.*

class BaseWebSocketDataSource {
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val messageChannel = Channel<String>(capacity = Channel.BUFFERED)

    private var webSocket: WebSocket? = null
    private val okHttpClient = OkHttpClient()

    @OptIn(DelicateCoroutinesApi::class)
    private val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            _connectionState.value = ConnectionState.CONNECTED
            Log.d("WebSocket", "WebSocket connected ${response.request.url}")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            if (!messageChannel.isClosedForSend) {
                if (!messageChannel.trySend(text).isSuccess) {
                    Log.e("WebSocket", "Failed to send message to channel: $text")
                }
            } else {
                Log.e("WebSocket", "Channel is closed, cannot send message")
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            _connectionState.value = ConnectionState.DISCONNECTED
            Log.d("WebSocket", "WebSocket closed: $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            _connectionState.value = ConnectionState.DISCONNECTED
            Log.e("WebSocket", "WebSocket connection failed: ${t.message}")
        }
    }

    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
    }


    fun disconnect() {
        webSocket?.close(1000, "Disconnected")
    }

    fun sendMessage(message: String) {
        Log.d("WebSocket", "WebSocket closed: $message")
        webSocket?.send(message)
    }

    fun receiveMessages(): Flow<String> {
        Log.d("WebSocket", "WebSocket receiveMessages" )
        return messageChannel.receiveAsFlow()
    }
}

enum class ConnectionState {
    CONNECTED, DISCONNECTED
}