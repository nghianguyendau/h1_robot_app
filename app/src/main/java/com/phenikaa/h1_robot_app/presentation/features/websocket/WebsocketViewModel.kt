package com.phenikaa.h1_robot_app.presentation.features.websocket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import com.phenikaa.h1_robot_app.data.datasource.websocket.ConnectionState
import com.phenikaa.h1_robot_app.domain.model.PhoneCallMessage
import com.phenikaa.h1_robot_app.domain.model.PhoneCallResponseMessage
import com.phenikaa.h1_robot_app.domain.usecase.websocket.ReceiveMessagesUseCase
import com.phenikaa.h1_robot_app.domain.usecase.websocket.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebSocketViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val receiveMessagesUseCase: ReceiveMessagesUseCase,
    private val webSocketRepository: WebSocketRepository
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    fun connect(url: String) {
        viewModelScope.launch {
            webSocketRepository.observeConnectionState().collect {
                _connectionState.value = it
            }
        }

        viewModelScope.launch {
            receiveMessagesUseCase.invoke().collect { message ->
                Log.d("WebSocketViewModel323232323232", "Received message: $message")
                _message.value = message
            }
        }
        viewModelScope.launch {
            webSocketRepository.connect(url)
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            webSocketRepository.disconnect()
        }
    }

    fun sendMessage(event: String, number: String) {
        viewModelScope.launch {
            val message = PhoneCallMessage(
                event = event,
                number = number
            )
            sendMessageUseCase(message.toString())
        }
    }
}
