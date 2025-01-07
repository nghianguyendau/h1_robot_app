package com.phenikaa.h1_robot_app.presentation.features.phonecall

sealed class PhoneCallUiState {
    object EnterRoomNumber: PhoneCallUiState()
    object EnterPhoneNumber : PhoneCallUiState()
    data class MovingToRoomNumber(val roomNumber: String) : PhoneCallUiState()
    data class OnCall(val status: CallStatus) : PhoneCallUiState()
    object Error : PhoneCallUiState()
}

sealed class CallStatus {
    object Calling : CallStatus()
    data class Ended(val reason: String) : CallStatus()
}

sealed class DeliveryTrayState {
    object Initial : DeliveryTrayState()
    object Opening : DeliveryTrayState()
    object Opened : DeliveryTrayState()
}