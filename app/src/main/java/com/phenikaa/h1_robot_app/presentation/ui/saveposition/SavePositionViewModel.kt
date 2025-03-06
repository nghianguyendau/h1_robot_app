package com.phenikaa.h1_robot_app.presentation.ui.saveposition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phenikaa.h1_robot_app.data.api.PhenikaaMecApiClient
import com.phenikaa.h1_robot_app.data.source.robot.RobotNaviDataSource
import com.phenikaa.h1_robot_app.data.model.NewPoint
import com.phenikaa.h1_robot_app.data.model.Point
import com.phenikaa.h1_robot_app.data.model.RosPosition
import com.phenikaa.h1_robot_app.domain.entity.Position
import com.phenikaa.h1_robot_app.domain.usecase.navigation.GetCurrentPositionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavePositionViewModel @Inject constructor(
    private val naviDataSource: RobotNaviDataSource,
    private val getCurrentPositionUseCase: GetCurrentPositionUseCase,

    ) : ViewModel(){
    private val _points = MutableStateFlow<List<Point>>(emptyList())
    private val _currentPosition = MutableStateFlow<Position?>(null)
    val currentPosition: StateFlow<Position?> = _currentPosition

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> get() = _currentPage

    private val _perPage = MutableStateFlow(10)
    val perPage: StateFlow<Int> get() = _perPage

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> get() = _totalPages


    val points: StateFlow<List<Point>> get() = _points

    fun getCurrentPosition() {
        Log.d("NavigationViewModel", "getCurrentPosition called")
        viewModelScope.launch {
            while (isActive) {
                try {
                    val position = getCurrentPositionUseCase()
                    _currentPosition.value = position
                } catch (e: Exception) {
                    Log.e("NavigationViewModel", "Error fetching position: ${e.message}")
                }
                delay(1000)
            }
//            try {
//                    val position = getCurrentPositionUseCase()
//                    _currentPosition.value = position
//                } catch (e: Exception) {
//                    Log.e("NavigationViewModel", "Error fetching position: ${e.message}")
//                }
        }
    }

    fun loadPoints(page: Int = 1) {
        viewModelScope.launch {
            try {
                val response = PhenikaaMecApiClient.apiService.getPoints(page, _perPage.value)

                if (response.data.points.isNotEmpty()) {
                    _points.value = response.data.points
                    _currentPage.value = page

                    val totalRecords = response.data.total
                    _totalPages.value = (totalRecords + _perPage.value - 1) / _perPage.value
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun savePoint(floorCode: String, name: String, type: Int) {
        viewModelScope.launch {
            try {
                val currentPosition = naviDataSource.getCurrentPosition()
                val newPoint = NewPoint(
                    floorCode = floorCode,
                    name = name ?: "Unknown",
                    x = currentPosition.pos.x,
                    y = currentPosition.pos.y,
                    z = currentPosition.pos.z,
                    rotation = currentPosition.pos.rotation,
                    type = type
                )

                val response = PhenikaaMecApiClient.apiService.savePoint(newPoint)
                if (response.isSuccessful) {
                    Log.d("API", "Thêm điểm mới thành công!")
                    loadPoints()
                } else {
                    Log.e("API", "Lỗi khi thêm điểm: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePoint(id: Int, floorCode: String, name: String, rosPosition: RosPosition, type: Int) {
        viewModelScope.launch {
            try {
                val updatedPoint = NewPoint(
                    floorCode = floorCode,
                    name = name,
                    x = rosPosition.pos.x,
                    y = rosPosition.pos.y,
                    z = rosPosition.pos.z,
                    rotation = rosPosition.pos.rotation,
                    type = type
                )

                val response = PhenikaaMecApiClient.apiService.updatePoint(id, updatedPoint)
                if (response.isSuccessful) {
                    Log.d("API", "Cập nhật điểm thành công!")
                    loadPoints()
                } else {
                    Log.e("API", "Lỗi khi cập nhật điểm: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun deletePoint(id: Int) {
        viewModelScope.launch {
            try {
                val response = PhenikaaMecApiClient.apiService.deletePoint(id)
                if (response.isSuccessful) {
                    Log.d("API", "Xóa điểm thành công!")
                    loadPoints()
                } else {
                    Log.e("API", "Lỗi khi xóa điểm: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Lỗi kết nối: ${e.message}")
            }
        }
    }
}
