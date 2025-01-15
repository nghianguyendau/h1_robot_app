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
import kotlinx.coroutines.delay
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

    private val _uiState = MutableStateFlow<PhoneCallUiState>(PhoneCallUiState.EnterRoomNumber)
    val uiState: StateFlow<PhoneCallUiState> = _uiState

    private val _trayState = MutableStateFlow<DeliveryTrayState>(DeliveryTrayState.Initial)
    val trayState: StateFlow<DeliveryTrayState> = _trayState

    private val _timeLeft = MutableStateFlow(140)
    val timeLeft: StateFlow<Int> = _timeLeft

    private var observeMessagesJob: Job? = null
    private var timerJob: Job? = null
    private var roomNumber: String = ""
    private var phoneNumber: String = ""

    init {
        viewModelScope.launch {
            observeConnectionStateUseCase().collect {
                _connectionState.value = it
            }
        }
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
        timerJob?.cancel()
    }

    fun setRoomNumber(number: String) {
        roomNumber = number
    }

    fun setPhoneNumber(number: String) {
        phoneNumber = number
    }

    fun getRoomNumber(): String = roomNumber
    fun getPhoneNumber(): String = phoneNumber

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1
            }
        }
    }

    fun updateState(state: PhoneCallUiState) {
        _uiState.value = state
        if (state is PhoneCallUiState.OnCall) {
            startTimer()
        }
    }

    fun makePhoneCall() {
        Log.d("PhoneCallViewModel", "makePhoneCall: $phoneNumber")
        viewModelScope.launch {
            makePhoneCallUseCase(phoneNumber)
        }
    }

    fun openTray() {
        viewModelScope.launch {
            _trayState.value = DeliveryTrayState.Opening
            delay(5000) // Simulate tray opening time
            _trayState.value = DeliveryTrayState.Opened
        }
    }

    private fun handlePhoneCallMessage(message: PhoneCallResponseMessage) {
        Log.d("HandleMessage", "Received message: $message")
        Log.d("HandleMessage", "Current uiState: ${_uiState.value}")

        when (message.status) {
            1, 2 -> {
                if (_uiState.value is PhoneCallUiState.MovingToRoomNumber) {
                    _uiState.value = PhoneCallUiState.OnCall(CallStatus.Calling)
                }
            }
            3, 4, 5 -> {
                if (_uiState.value is PhoneCallUiState.OnCall) {
                    _uiState.value = PhoneCallUiState.OnCall(CallStatus.Ended("Cuộc gọi kết thúc."))
                }
            }
            else -> {
                _uiState.value = PhoneCallUiState.Error
            }
        }
    }

    fun reset() {
        _uiState.value = PhoneCallUiState.EnterPhoneNumber
        _trayState.value = DeliveryTrayState.Initial
        _timeLeft.value = 140
        _phoneCallState.value = null
        roomNumber = ""
        phoneNumber = ""
        timerJob?.cancel()
        observeMessagesJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}