package com.phenikaa.h1_robot_app.presentation.features.phonecall

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phenikaa.h1_robot_app.data.datasource.websocket.ConnectionState
import com.phenikaa.h1_robot_app.domain.model.PhoneCallResponseMessage
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.ConnectPhoneCallWebSocketUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.DisconnectPhoneCallWebSocketUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.MakePhoneCallUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.ObserveConnectionStateUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.ObservePhoneCallMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneCallViewModel @Inject constructor(
    private val connectUseCase: ConnectPhoneCallWebSocketUseCase,
    private val disconnectUseCase: DisconnectPhoneCallWebSocketUseCase,
    private val makePhoneCallUseCase: MakePhoneCallUseCase,
    private val observeMessagesUseCase: ObservePhoneCallMessagesUseCase,
    private val observeConnectionStateUseCase: ObserveConnectionStateUseCase
) : ViewModel() {
    private val _phoneCallState = MutableStateFlow<PhoneCallResponseMessage?>(null)
    val phoneCallState: StateFlow<PhoneCallResponseMessage?> = _phoneCallState

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _uiState = MutableStateFlow<PhoneCallUiState>(PhoneCallUiState.Calling)
    val uiState: StateFlow<PhoneCallUiState> = _uiState

    private var observeMessagesJob: Job? = null
    private var roomNumber: String = ""

    init {
        viewModelScope.launch {
            observeConnectionStateUseCase().collect {
                _connectionState.value = it
            }
        }

//        viewModelScope.launch {
//            observeMessagesUseCase().collect { message ->
//                handlePhoneCallMessage(message)
//            }
//        }
    }

    fun connect(url: String) {
        connectUseCase(url)
        observeMessagesJob?.cancel()
        observeMessagesJob = viewModelScope.launch {
            observeMessagesUseCase().collect { message ->
                handlePhoneCallMessage(message)
            }
        }
    }

    fun disconnect() {
        disconnectUseCase()
        observeMessagesJob?.cancel()
    }

    fun setRoomNumber(number: String) {
        roomNumber = number
    }

    fun getRoomNumber(): String = roomNumber

    fun updateState(state: PhoneCallUiState) {
        _uiState.value = state
    }

    fun makePhoneCall(phoneNumber: String) {
        Log.d("PhoneCallViewModel", "makePhoneCall: $phoneNumber")
        viewModelScope.launch {
            makePhoneCallUseCase(phoneNumber)
        }
    }

    private fun handlePhoneCallMessage(message: PhoneCallResponseMessage) {
        Log.d("HandleMessage", "Received message: $message")
        Log.d("HandleMessage", "Current uiState: ${_uiState.value}")

        when (message.status) {
            1, 2 -> {
                if (_uiState.value == PhoneCallUiState.EnterPhoneNumber) {
                    _uiState.value = PhoneCallUiState.Calling
                    Log.d("HandleMessage", "State updated to Calling")
                }
            }
            3, 4, 5 -> {
                if (_uiState.value == PhoneCallUiState.Calling) {
                    _uiState.value = PhoneCallUiState.CallEnded("Cuộc gọi kết thúc.")
                    Log.d("HandleMessage", "State updated to CallEnded")
                }
            }
            else -> {
                _uiState.value = PhoneCallUiState.Error
                Log.d("HandleMessage", "State updated to Error")
            }
        }
    }

    fun reset() {
        _uiState.value = PhoneCallUiState.EnterPhoneNumber
        _phoneCallState.value = null
        roomNumber = ""
        observeMessagesJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}