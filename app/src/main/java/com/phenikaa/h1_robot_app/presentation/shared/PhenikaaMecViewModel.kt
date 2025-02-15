package com.phenikaa.h1_robot_app.presentation.shared

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phenikaa.h1_robot_app.data.model.DataFloors
import com.phenikaa.h1_robot_app.data.model.NewPoint
import com.phenikaa.h1_robot_app.data.model.Point
import com.phenikaa.h1_robot_app.domain.usecase.floor.GetAllFloorsUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.DeletePointUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.GetPointsByFloorIdUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.GetPointsUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.SavePointUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.UpdatePointUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val getAllFloorsUseCase: GetAllFloorsUseCase
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
                            floor = null  // Nếu cần thông tin về floor, có thể thêm vào đây
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
}