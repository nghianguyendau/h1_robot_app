package com.phenikaa.h1_robot_app.presentation.features.elevator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.phenikaa.h1_robot_app.data.model.RosPosition
import com.phenikaa.h1_robot_app.data.repository.ElevatorRepository
import com.phenikaa.h1_robot_app.domain.model.Position
import com.phenikaa.h1_robot_app.domain.usecase.elevator.CallElevatorUseCase
import com.phenikaa.h1_robot_app.domain.usecase.elevator.MonitorElevatorTaskUseCase
import com.phenikaa.h1_robot_app.presentation.features.navigation.NavigationViewModel
import com.phenikaa.h1_robot_app.state.RobotState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RobotElevatorViewModel @Inject constructor(
    private val elevatorRepository: ElevatorRepository,

) : ViewModel() {
    lateinit var navigationViewModel: NavigationViewModel

    fun initNavigationViewModel(owner: ViewModelStoreOwner) {
        navigationViewModel = ViewModelProvider(owner)[NavigationViewModel::class.java]
    }
    private val _robotState = MutableStateFlow<RobotState>(RobotState.IDLE)
    val robotState: StateFlow<RobotState> = _robotState

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages

    init {
        connectWebSocket()
        listenToElevatorMessages()
    }

    fun callElevator() {
        viewModelScope.launch {
            try {
                val response = elevatorRepository.callElevator(
                    currentFloor = 13,
                    destinationFloor = 1,
                )
                Log.d("Elevator", "Response: $response")
            } catch (e: Exception) {
                Log.e("Elevator", "Error calling elevator: ${e.message}")
            }
        }
    }

    private fun connectWebSocket() = elevatorRepository.connect()

    private fun listenToElevatorMessages() {
        viewModelScope.launch {
            elevatorRepository.receiveMessages().collect { message ->
                _messages.value = _messages.value + message
                handleElevatorMessage(message)
            }
        }
    }

    private fun handleElevatorMessage(message: String) {
        when {
            message.contains("elevator-arrived") -> {
                _robotState.value = RobotState.MOVING_TO_CABIN

                navigateToCabin()
            }
            message.contains("elevator-arrived-to-destination") -> {
                _robotState.value = RobotState.MOVING_TO_DESTINATION
            }
        }
    }

    fun callElevator(currentFloor: Int, destinationFloor: Int) {
        _robotState.value = RobotState.CALLING_ELEVATOR
        val callMessage = """{
            "action": "call-elevator",
            "current_floor": $currentFloor,
            "destination_floor": $destinationFloor
        }"""
        elevatorRepository.sendMessage(callMessage)
        _robotState.value = RobotState.WAITING_FOR_ELEVATOR
    }

    fun sendRobotConfirmation(eventId: String) {
        val confirmationMessage = """{
            "action": "robot-confirmation",
            "event_id": "$eventId"
        }"""
        elevatorRepository.sendMessage(confirmationMessage)
    }

    fun navigateToCabin(){
        val cabinPosition = Position(
            x = 1.8457481f, // Giá trị x của vị trí cabin
            y = 2.5407f, // Giá trị y của vị trí cabin
            z = 0.0f,
            rotation = -0.8438944f,
            poseName = "Cabin Entrance"
        )

        // Gọi điều hướng qua NavigationViewModel
        navigationViewModel.navigateToPosition(RosPosition.fromDomainModel(cabinPosition))
    }
}
