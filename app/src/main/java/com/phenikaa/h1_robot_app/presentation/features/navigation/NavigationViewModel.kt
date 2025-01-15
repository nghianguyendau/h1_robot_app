package com.phenikaa.h1_robot_app.presentation.features.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phenikaa.h1_robot_app.data.datasource.robot.RobotNaviDataSource
import com.phenikaa.h1_robot_app.data.model.RosPosition
import com.phenikaa.h1_robot_app.domain.model.Position
import com.phenikaa.h1_robot_app.domain.model.NavigationState
import com.phenikaa.h1_robot_app.domain.usecase.navigation.GetCurrentPositionUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.MoveDirectionUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToPositionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val getCurrentPositionUseCase: GetCurrentPositionUseCase,
    private val navigateToPositionUseCase: NavigateToPositionUseCase,
    private val moveDirection: MoveDirectionUseCase,
    private val naviDataSource: RobotNaviDataSource

) : ViewModel() {
    private var movementJob: Job? = null

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.Idle)
    val navigationState: StateFlow<NavigationState> = _navigationState

    private val _currentPosition = MutableStateFlow<Position?>(null)
    val currentPosition: StateFlow<Position?> = _currentPosition

    private val _currentSpeed = MutableStateFlow<Float?>(null)
    val currentSpeed: StateFlow<Float?> get() = _currentSpeed

    fun getCurrentPosition() {
        Log.d("NavigationViewModel", "getCurrentPosition called")
        viewModelScope.launch {
            while (true) {
                try {
                    val position = getCurrentPositionUseCase()
                    _currentPosition.value = position
                } catch (e: Exception) {
                    Log.e("NavigationViewModel", "Error fetching position: ${e.message}")
                }
                delay(1000)
            }
        }
    }


    fun navigateToPosition(position: RosPosition) {
        viewModelScope.launch {
//            navigateToPositionUseCase(position).collect { state ->
//                _navigationState.value = state
//            }
//            navigateToPositionUseCase.navigateToPosition(position)
        }
    }

    fun moveDirection(direction: Int){
        viewModelScope.launch {
            moveDirection.moveDirection(direction)
        }
    }

    fun moveBySerial(direction: Int){
        viewModelScope.launch {
            moveDirection.moveBySerial(direction)
        }
    }

    fun moveSerial(linear: Int, angular: Int){

        viewModelScope.launch {
            while (isActive) { // Kiểm tra trạng thái coroutine
                moveDirection.moveSerial(linear, angular)
                delay(50) // Gửi lệnh mỗi 50ms
            }
        }
    }

    fun stopMovement() {
        // Hủy job để dừng gửi lệnh
        movementJob?.cancel()
    }

    fun goAngle(angle: Int){
        viewModelScope.launch {
            moveDirection.goAngle(angle)
        }
    }

    fun moveAngle(angle: Int){
        viewModelScope.launch {
           moveDirection.moveAngle(angle)
        }
    }

    fun goHome(){
        viewModelScope.launch {
            moveDirection.goHome()
        }
    }


    private val _savedPosition = MutableStateFlow<RosPosition?>(null)
    val savedPosition: StateFlow<RosPosition?> get() = _savedPosition

    private val _navigationResult = MutableStateFlow<Boolean?>(null)
    val navigationResult: StateFlow<Boolean?> get() = _navigationResult

    fun saveCurrentPosition() {
        viewModelScope.launch {
            try {
                val currentPosition = naviDataSource.getCurrentPosition()
                _savedPosition.value = currentPosition
                Log.d("NavigationViewModel", "Saved position: $currentPosition")
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error saving position: ${e.message}")
            }
        }
    }

    fun navigateToSavedPosition() {
        val position = _savedPosition.value
        if (position == null) {
            Log.e("NavigationViewModel", "No position saved to navigate to")
            return
        }

        viewModelScope.launch {
            val result = navigateToPositionUseCase(position)
            _navigationResult.value = result
//            navigateToPositionUseCase.navigateToPosition(position)
        }
    }

    fun cancelNavi(){
        viewModelScope.launch {
            navigateToPositionUseCase.cancelNavi()
        }
    }

    fun setSpeed(speed: Float){
        viewModelScope.launch {
            navigateToPositionUseCase.setSpeed(speed)
        }
    }

    fun fetchCurrentSpeed() {
        viewModelScope.launch {
            try {
                val speed = navigateToPositionUseCase.getSpeed()
                _currentSpeed.value = speed
                Log.d("NavigationViewModel", "Current speed: $speed")
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error fetching speed: ${e.message}")
            }
        }
    }
}
