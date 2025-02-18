package com.phenikaa.h1_robot_app.presentation.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phenikaa.h1_robot_app.data.datasource.websocket.ConnectionState
import com.phenikaa.h1_robot_app.data.model.DataFloors
import com.phenikaa.h1_robot_app.data.model.NewPoint
import com.phenikaa.h1_robot_app.data.model.Point
import com.phenikaa.h1_robot_app.domain.usecase.floor.GetAllFloorsUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.MoveDirectionUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToDestinationUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToPositionUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.ConnectPhoneCallWebSocketUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.DisconnectPhoneCallWebSocketUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.ObserveConnectionStateUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.ObservePhoneCallMessagesUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.SendLoraMessageUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.DeletePointUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.GetPointsByFloorIdUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.GetPointsUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.SavePointUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.UpdatePointUseCase
import com.phenikaa.h1_robot_app.utils.NavigationUtils.toNavigationStrings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhenikaaMecViewModel @Inject constructor(
    private val getPointsUseCase: GetPointsUseCase,
    private val getPointsByFloorIdUseCase: GetPointsByFloorIdUseCase,
    private val savePointUseCase: SavePointUseCase,
    private val updatePointUseCase: UpdatePointUseCase,
    private val deletePointUseCase: DeletePointUseCase,
    private val getAllFloorsUseCase: GetAllFloorsUseCase,
    private val navigateToDestinationUseCase: NavigateToDestinationUseCase,
    private val navigateToPositionUseCase: NavigateToPositionUseCase,
    private val moveDirection: MoveDirectionUseCase,
    private val connectUseCase: ConnectPhoneCallWebSocketUseCase,
    private val disconnectUseCase: DisconnectPhoneCallWebSocketUseCase,
    private val sendLoraMessageUseCase: SendLoraMessageUseCase,
    private val observeConnectionStateUseCase: ObserveConnectionStateUseCase
) : ViewModel() {

    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points: StateFlow<List<Point>> = _points.asStateFlow()

    private val _floors = MutableStateFlow<List<DataFloors>>(emptyList())
    val floors: StateFlow<List<DataFloors>> = _floors.asStateFlow()

    private val _selectedPoints = MutableStateFlow<Set<Point>>(emptySet())
    val selectedPoints: StateFlow<Set<Point>> = _selectedPoints.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.Idle)
    val navigationState: StateFlow<NavigationState> = _navigationState.asStateFlow()

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    sealed class NavigationState {
        object Idle : NavigationState()
        data class PointCompleted(val pointName: String) : NavigationState()
        object AllPointsCompleted : NavigationState()
    }

    init {
        viewModelScope.launch {
            observeConnectionStateUseCase().collect {
                _connectionState.value = it
            }
        }
    }


    fun getPoints(page: Int = 1, perPage: Int = 20) {
        viewModelScope.launch {
            _isLoading.value = true
            getPointsUseCase(page, perPage)
                .onSuccess { response ->
                    _points.value = response.data.points
                    _error.value = null

                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun getPointsByFloorId(floorId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            getPointsByFloorIdUseCase(floorId)
                .onSuccess { response ->
                    Log.e("hhhhhhhhhhhh", "Received data: ${response.data}")

                    // Chuyển đổi từ List<DataPointsByFloorIdWrapper> sang List<Point>
                    _points.value = response.data.map { item ->
                        Point(
                            id = item.id,
                            floorCode = item.code,
                            name = item.name,
                            x = item.x,
                            y = item.y,
                            z = item.z,
                            rotation = item.rotation,
                            type = item.type,
                            floor = null
                        )
                    }
                    _error.value = null
                }
                .onFailure { exception ->
                    Log.e("VIEWMODEL_ERROR", "Error fetching points: ${exception.message}")
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }


    fun getAllFloors(page: Int = 1, perPage: Int = 20) {
        viewModelScope.launch {
            _isLoading.value = true
            getAllFloorsUseCase(page, perPage)
                .onSuccess { response ->
                    _floors.value = response.data.floors
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun savePoint(point: NewPoint) {
        viewModelScope.launch {
            _isLoading.value = true
            savePointUseCase(point)
                .onSuccess {
                    getPoints()
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun updatePoint(id: Int, point: NewPoint) {
        viewModelScope.launch {
            _isLoading.value = true
            updatePointUseCase(id, point)
                .onSuccess {
                    getPoints()
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun deletePoint(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            deletePointUseCase(id)
                .onSuccess {
                    getPoints()
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun togglePointSelection(point: Point) {
        _selectedPoints.value = if (point in _selectedPoints.value) {
            _selectedPoints.value - point
        } else {
            _selectedPoints.value + point
        }
    }

    fun clearSelectedPoints() {
        _selectedPoints.value = emptySet()
    }

    fun navigateMultiplePoints() {
        viewModelScope.launch {
            try {
                selectedPoints.value.toNavigationStrings().forEachIndexed { index, position ->
                    val point = selectedPoints.value.elementAt(index)

                    val result = navigateToDestinationUseCase(position)

                    if (result) {
                        _navigationState.value = NavigationState.PointCompleted(point.name ?: "Điểm ${index + 1}")
                        point.name?.let { sendLoraMessageUseCase(loraMessage = it) }
                        delay(10000)
                        sendLoraMessageUseCase(loraMessage = "")
                    } else {
                    }
                }

                _navigationState.value = NavigationState.AllPointsCompleted
                clearSelectedPoints()

            } catch (e: Exception) {
            } finally {
                delay(1000)
                _navigationState.value = NavigationState.Idle
            }
        }
    }

    fun cancelNavi(){
        viewModelScope.launch {
            navigateToPositionUseCase.cancelNavi()
        }
    }

    fun goHome(){
        viewModelScope.launch {
            moveDirection.goHome()
        }
    }

    fun connectWebsocket(url: String) {
        connectUseCase(url)

    }

    fun disconnectWebsocket() {
        disconnectUseCase()
    }

    override fun onCleared() {
        super.onCleared()
        disconnectWebsocket()
    }
}