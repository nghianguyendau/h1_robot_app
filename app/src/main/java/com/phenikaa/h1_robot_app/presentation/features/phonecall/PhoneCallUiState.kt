package com.phenikaa.h1_robot_app.presentation.features.phonecall

sealed class PhoneCallUiState {
    object EnterRoomNumber: PhoneCallUiState()
    object EnterPhoneNumber : PhoneCallUiState()
    data class MovingToRoomNumber(val roomNumber: String) : PhoneCallUiState()
    object Calling : PhoneCallUiState()
    data class CallEnded(val reason: String) : PhoneCallUiState()
    object Error : PhoneCallUiState()
}
