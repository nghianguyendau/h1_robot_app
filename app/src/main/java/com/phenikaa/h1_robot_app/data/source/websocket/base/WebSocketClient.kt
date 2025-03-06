package com.phenikaa.h1_robot_app.data.source.websocket.base

import android.util.Log
import com.phenikaa.h1_robot_app.data.source.websocket.ConnectionState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketClient {
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val messageChannel = Channel<String>(capacity = Channel.BUFFERED)

    private var webSocket: WebSocket? = null
    private val okHttpClient = OkHttpClient()

    @OptIn(DelicateCoroutinesApi::class)
    private val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            _connectionState.value = ConnectionState.CONNECTED
            Log.d("WebSocketRoute", "WebSocket connected ${response.request.url}")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.e("WebSocketRoute", "onMessage: $text")
            if (!messageChannel.isClosedForSend) {
                if (!messageChannel.trySend(text).isSuccess) {
                    Log.e("WebSocketRoute", "Failed to send message to channel: $text")
                }
            } else {
                Log.e("WebSocketRoute", "Channel is closed, cannot send message")
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            _connectionState.value = ConnectionState.DISCONNECTED
            Log.d("WebSocketRoute", "WebSocket closed: $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            _connectionState.value = ConnectionState.DISCONNECTED
            Log.e("WebSocketRoute", "WebSocket connection failed: ${t.message}")
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
        webSocket?.send(message)
    }

    fun receiveMessages(): Flow<String> {
        return messageChannel.receiveAsFlow()
    }
}