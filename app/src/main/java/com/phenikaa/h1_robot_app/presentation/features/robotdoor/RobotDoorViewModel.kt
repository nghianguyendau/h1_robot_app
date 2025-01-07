package com.phenikaa.h1_robot_app.presentation.features.robotdoor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csjbot.coshandler.listener.OnDoubleDoorStateListener
import com.phenikaa.h1_robot_app.domain.usecase.robotdoor.RobotDoorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RobotDoorViewModel @Inject constructor(
    private val doorControlUseCase: RobotDoorUseCase
) : ViewModel() {

    private val _doorState = MutableStateFlow<Pair<Int, Int>?>(null)
    val doorState: StateFlow<Pair<Int, Int>?> = _doorState.asStateFlow()

    fun openDoor() {
        Log.d("ViewModel", "openDoor() called")
        viewModelScope.launch {
            doorControlUseCase.openDoor { i1, i2 ->
                Log.d("ViewModel", "openDoor() result: state1=$i1, state2=$i2")
                _doorState.value = Pair(i1, i2)
            }
        }
    }

    fun closeDoor() {
        viewModelScope.launch {
            doorControlUseCase.closeDoor { i1, i2 -> _doorState.value = Pair(i1, i2) }
        }
    }

    fun openOneFloorDoor() {
        viewModelScope.launch {
            doorControlUseCase.openOneFloorDoor()
        }
    }

    fun openTwoFloorDoor() {
        viewModelScope.launch {
            doorControlUseCase.openTwoFloorDoor()
        }
    }

    fun closeOneFloorDoor() {
        viewModelScope.launch {
            doorControlUseCase.closeOneFloorDoor()
        }
    }

    fun closeTwoFloorDoor() {
        viewModelScope.launch {
            doorControlUseCase.closeTwoFloorDoor()
        }
    }
}