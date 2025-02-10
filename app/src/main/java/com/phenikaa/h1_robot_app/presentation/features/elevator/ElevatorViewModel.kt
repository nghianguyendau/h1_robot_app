package com.phenikaa.h1_robot_app.presentation.features.elevator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.phenikaa.h1_robot_app.data.datasource.robot.RobotNaviDataSource
import com.phenikaa.h1_robot_app.data.model.RosPosition
import com.phenikaa.h1_robot_app.data.repository.ElevatorRepository
import com.phenikaa.h1_robot_app.domain.model.Position
import com.phenikaa.h1_robot_app.domain.usecase.elevator.CallElevatorUseCase
import com.phenikaa.h1_robot_app.domain.usecase.elevator.MonitorElevatorTaskUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.MapUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToDestinationUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToPosition2UseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToPositionUseCase
import com.phenikaa.h1_robot_app.presentation.features.navigation.NavigationViewModel
import com.phenikaa.h1_robot_app.state.RobotState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RobotElevatorViewModel @Inject constructor(
    private val elevatorRepository: ElevatorRepository,
    private val navigateToPositionUseCase: NavigateToPositionUseCase,
    private val navigateToPosition2UseCase: NavigateToPosition2UseCase,
    private val navigateToDestinationUseCase: NavigateToDestinationUseCase,
    private val naviDataSource: RobotNaviDataSource,
    private val mapUseCase: MapUseCase
) : ViewModel() {

    lateinit var navigationViewModel: NavigationViewModel

    fun initNavigationViewModel(owner: ViewModelStoreOwner) {
        navigationViewModel = ViewModelProvider(owner)[NavigationViewModel::class.java]
    }

    private val _robotState = MutableStateFlow<RobotState>(RobotState.IDLE)
    val robotState: StateFlow<RobotState> = _robotState

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages

    private val _savedPosition1 = MutableStateFlow<RosPosition?>(null)
    private val _savedPosition2 = MutableStateFlow<RosPosition?>(null)

    private val _navigationResult = MutableStateFlow<Boolean?>(null)
    val navigationResult: StateFlow<Boolean?> = _navigationResult

    init {
//        connectWebSocket()
//        listenToElevatorMessages()
    }

    // Gọi API gọi thang máy
    fun callElevator(currentFloor: Int, destinationFloor: Int) {
        _robotState.value = RobotState.CALLING_ELEVATOR
        viewModelScope.launch {
            try {
                val response = elevatorRepository.callElevator(
                    currentFloor = currentFloor,
                    destinationFloor = destinationFloor
                )
                Log.d("Elevator", "Response: $response")


                _robotState.value = RobotState.WAITING_FOR_ELEVATOR
                navigateToElevatorPoint()
            } catch (e: Exception) {
                Log.e("Elevator", "Error calling elevator: ${e.message}")
            }
        }
    }

    // Kết nối WebSocket
//    private fun connectWebSocket() {
//        elevatorRepository.connect()
//    }

    // Lắng nghe tin nhắn từ WebSocket
    private fun listenToElevatorMessages() {
        viewModelScope.launch {
            elevatorRepository.receiveMessages().collect { message ->
                _messages.value = _messages.value + message
//                handleElevatorMessage(message)
            }
        }
    }

    // Xử lý tin nhắn WebSocket
    private val _taskId = MutableStateFlow<Int?>(null)
    val taskId: StateFlow<Int?> = _taskId

//    private fun handleElevatorMessage(message: String) {
//        try {
//            Log.d("ElevatorMessage", "Received message: $message")
//
//            val jsonObject = JSONObject(message)
//            val event = jsonObject.getString("event")
//
//            if (event == "elevator-arrived-to-pick-up-robot") {
//                val data = jsonObject.getJSONObject("data")
//                if (data.has("task_id")) {
//                    val taskId = data.getInt("task_id")
//                    _taskId.value = taskId // Lưu task_id
//                    Log.d("ElevatorMessage", "Task ID received: $taskId")
//
//                    if (jsonObject.has("msg_id")) {
//                        val msgId = jsonObject.getString("msg_id")
//                        sendRobotConfirmation(msgId) // Gửi xác nhận
//                    }
//
////                    processElevatorArrived() // Xử lý tiếp tục
//                    waitForElevatorPointAndProceed()
//                }
//                else {
//                    Log.e("ElevatorMessage", "Invalid payload: Missing 'task_id'")
//                }
//            }
//            else if(event == "elevator-arrived-to-destination"){
//                val data = jsonObject.getJSONObject("data")
//                val mapName = "1-2"
//                val x =  -13.975249f // Giá trị x cố định
//                val y = -0.41427234f // Giá trị y cố định
//                val rotation = 93.914665f // Giá trị rotation cố định
//                if (data.has("task_id")) {
//                    val msgId = jsonObject.getString("msg_id")
//                    val taskId = data.getInt("task_id")
//                    Log.d(
//                        "ElevatorMessage",
//                        "Elevator arrived at destination. Task ID: $taskId"
//                    )
//                    sendRobotConfirmation(msgId) // Gửi xác nhận
////                    loadMapByName(mapName)
////                    loadMapToPosition(mapName, x, y, rotation)
//                    _robotState.value = RobotState.MOVING_TO_DESTINATION
//
////                    moveDirectionRepeatedly(10, 0)
//                    navigateToExitCabin()
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("ElevatorMessage", "Error parsing elevator message: ${e.message}")
//        }
//    }


    private fun moveDirectionRepeatedly(times: Int, direction: Int) {
        viewModelScope.launch {
            try {
                repeat(times) {
                   navigationViewModel.moveDirection(direction) // Gọi hàm với direction là 0
                    kotlinx.coroutines.delay(100) // Thời gian giữa các lần gọi (100ms)
                }
                Log.d("MoveDirection", "Completed $times calls to moveDirection with direction $direction")
            } catch (e: Exception) {
                Log.e("MoveDirection", "Error in moving direction: ${e.message}")
            }
        }
    }



    // Gửi xác nhận robot
    private fun sendRobotConfirmation(msgId: String) {
        val confirmationMessage = JSONObject().apply {
            put("event", "robot-confirmation")
            put("data", JSONObject().apply {
                put("msg_id", msgId)
            })
        }.toString()

        viewModelScope.launch {
            try {
                elevatorRepository.sendMessage(confirmationMessage)
                Log.d("WebSocket", "Sent robot confirmation: $confirmationMessage")
            } catch (e: Exception) {
                Log.e("WebSocket", "Error sending robot confirmation: ${e.message}")
            }
        }
    }

    fun sendRobotInCabinMessage(taskId: Int) {
        val message = JSONObject().apply {
            put("event", "robot-went-in-cabin")
            put("data", JSONObject().apply {
                put("msg_id", "robot-went-in-cabin_$taskId")
                put("task_id", taskId)
            })
        }.toString()

        viewModelScope.launch {
            try {
                elevatorRepository.sendMessage(message)
                Log.d("WebSocket", "Sent robot in cabin message: $message")
            } catch (e: Exception) {
                Log.e("WebSocket", "Error sending robot in cabin message: ${e.message}")
            }
        }
    }

    // Thêm trạng thái để theo dõi tiến trình di chuyển
    private val _isElevatorPointReached = MutableStateFlow(false)

    private fun waitForElevatorPointAndProceed() {
        viewModelScope.launch {
            // Chờ cho đến khi navigateToElevatorPoint hoàn thành
            _isElevatorPointReached.collect { isReached ->
                if (isReached) {
                    processElevatorArrived() // Thực hiện tiếp hành động sau khi đã đến
                    _isElevatorPointReached.value = false // Reset trạng thái
                }
            }
        }
    }

    private fun processElevatorArrived() {
        _robotState.value = RobotState.MOVING_TO_CABIN
        navigateToCabin()
    }

    fun cancelAllTasks() {
        val message = JSONObject().apply {
            put("event", "cancel-all-task")
            put("data", JSONObject().apply {
                put("serial_number", "SN01") // Thay SN01 bằng serial number thực tế nếu cần
            })
        }.toString()

        viewModelScope.launch {
            try {
                elevatorRepository.sendMessage(message)
                Log.d("WebSocket", "Sent cancel all tasks message: $message")
            } catch (e: Exception) {
                Log.e("WebSocket", "Error sending cancel all tasks message: ${e.message}")
            }
        }
    }


    // Lưu vị trí điểm trước thang máy
    fun saveCurrentPosition1() {
        viewModelScope.launch {
            try {
                val currentPosition = naviDataSource.getCurrentPosition()
                _savedPosition1.value = currentPosition
                Log.d("NavigationViewModel", "Saved elevator point: $currentPosition")
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error saving elevator point: ${e.message}")
            }
        }
    }

    // Lưu vị trí trong cabin thang máy
    fun saveCurrentPosition2() {
        viewModelScope.launch {
            try {
                val currentPosition = naviDataSource.getCurrentPosition()
                _savedPosition2.value = currentPosition
                Log.d("NavigationViewModel", "Saved cabin point: $currentPosition")
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error saving cabin point: ${e.message}")
            }
        }
    }

    // Di chuyển đến điểm trước thang máy
    fun navigateToElevatorPoint() {
//        val position = "x= 16.090723f, y=-7.5906825f, z=0.0f, rotation=92.71033f"
        val position = "x= 2.6824255f, y=-0.2290653f, z=0.0f, rotation=-0.2657993f"

//        if (position == null) {
//            Log.e("NavigationViewModel", "No elevator point saved to navigate to")
//            return
//        }

        viewModelScope.launch {
            try {
                _isElevatorPointReached.value = false // Đặt trạng thái chưa đến
                val result = navigateToPositionUseCase(position)
                _navigationResult.value = result
                Log.d("NavigationViewModel", "Navigation to elevator point result: $result")
                if (result == true) {
                    _isElevatorPointReached.value = true // Đánh dấu đã đến
                    Log.d("NavigationViewModel", "Reached elevator point")
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error navigating to elevator point: ${e.message}")
            }
        }
    }

    // Di chuyển vào cabin thang máy
    fun navigateToCabin() {
//        val position = _savedPosition2.value
//        val position = "x= 15.971256f, y=-4.607306f, z=0.0f, rotation=-87.812195f"
        val position = "x= -0.21469636f, y=0.006519f, z=0.0f, rotation=-0.19207564f"
        if (position == null) {
            Log.e("NavigationViewModel", "No cabin point saved to navigate to")
            return
        }

        viewModelScope.launch {
            try {
                val result = navigateToPosition2UseCase(position)
                _navigationResult.value = result
                Log.d("NavigationViewModel", "Navigation to cabin result: $result")

                if (result == true) {
                    // Lấy task_id từ StateFlow
                    val currentTaskId = _taskId.value
                    val mapName = "1-2"
                    if (currentTaskId != null) {
                        sendRobotInCabinMessage(currentTaskId) // Gửi thông điệp
                        _robotState.value = RobotState.INSIDE_CABIN
                        loadMapByName(mapName)
                    } else {
                        Log.e("NavigationViewModel", "Task ID is null, cannot send robot in cabin message")
                    }
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error navigating to cabin: ${e.message}")
            }
        }
    }
    fun navigateToExitCabin() {
        // Định nghĩa vị trí mà robot cần đi ra ngoài cabin
//        val position = "x= -13.873219f, y=2.7112331f, z=0.0f, rotation=87.985886f"
        val position = "x= 2.6824255f, y=-0.2290653f, z=0.0f, rotation=-0.2657993f"

        if (position == null) {
            Log.e("NavigationViewModel", "No exit position saved to navigate to")
            return
        }

        viewModelScope.launch {
            try {
                // Điều hướng robot đến vị trí ra ngoài cabin
//                val result = navigateToDestinationUseCase(position)
                val result = navigateToPositionUseCase(position)
                _navigationResult.value = result
                Log.d("NavigationViewModel", "Navigation to exit cabin result: $result")

                if (result == true) {
                    // Lấy task_id từ StateFlow để gửi xác nhận cho Cloud
                    val currentTaskId = _taskId.value
                    if (currentTaskId != null) {
                        sendRobotExitCabinMessage(currentTaskId) // Gửi thông điệp đến Cloud
                        Log.d("NavigationViewModel", "Robot successfully exited the cabin and sent confirmation.")
                    } else {
                        Log.e("NavigationViewModel", "Task ID is null, cannot send robot exit cabin message")
                    }
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error navigating to exit cabin: ${e.message}")
            }
        }
    }

    fun sendRobotExitCabinMessage(taskId: Int) {
        val message = JSONObject().apply {
            put("event", "robot-went-out-cabin")
            put("data", JSONObject().apply {
                put("msg_id", "robot-went-out-cabin_$taskId")
                put("task_id", taskId)
            })
        }.toString()

        viewModelScope.launch {
            try {
                elevatorRepository.sendMessage(message)
                Log.d("WebSocket", "Sent robot exit cabin message: $message")
            } catch (e: Exception) {
                Log.e("WebSocket", "Error sending robot exit cabin message: ${e.message}")
            }
        }
    }



    //    private val _selectedMap = MutableStateFlow<String?>(null)
//    fun loadMap() {
//        val mapName = "1"
////        if (mapName.isNullOrEmpty()) {
////            Log.e("NavigationViewModel", "No map selected to load")
////            return
////        }
//
//        viewModelScope.launch {
//            try {
//                mapUseCase.loadMapToPosition(mapName, 15.91606f, -4.629563f, -87.43603f)
//                Log.d("ElevatorModel", "Map loaded: $mapName")
//            } catch (e: Exception) {
//                Log.e("ElevatorModel", "Error loading map: ${e.message}")
//            }
//        }
//    }
    fun loadMapByName(name: String){
        viewModelScope.launch {
            mapUseCase.loadMapByName(name)
            Log.d("NavigationViewModel", "Map loaded: $name")
        }
    }

    fun loadMapToPosition(name: String, x: Float, y: Float, rotation: Float) {
        viewModelScope.launch {
            try {
                // Lưu Locale gốc
                val originalLocale = Locale.getDefault()

                // Tạm thời ép hệ thống dùng Locale.US
                Locale.setDefault(Locale.US)
//                val mapName = "1"
//                val x = 1.007645f
//                val y = 0.955442f
//                val rotation = 36.040421f

                // Gọi hàm load map với vị trí chính xác
                mapUseCase.loadMapToPosition(name, x, y, rotation)

                Log.d("NavigationViewModel", "Map loaded successfully: $name at ($x, $y, $rotation)")

                // Khôi phục Locale gốc sau khi load xong
                Locale.setDefault(originalLocale)

            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error loading map: ${e.message}")
            }
        }
    }
}
