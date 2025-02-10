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
import com.phenikaa.h1_robot_app.domain.usecase.navigation.MoveDirectionUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToDestinationUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToPosition2UseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToPositionUseCase
import com.phenikaa.h1_robot_app.presentation.features.navigation.NavigationViewModel
import com.phenikaa.h1_robot_app.state.RobotState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ElevatorGoHomeViewModel @Inject constructor(
    private val elevatorRepository: ElevatorRepository,
    private val navigateToPositionUseCase: NavigateToPositionUseCase,
    private val navigateToPosition2UseCase: NavigateToPosition2UseCase,
    private val navigateToDestinationUseCase: NavigateToDestinationUseCase,
    private val naviDataSource: RobotNaviDataSource,
    private val mapUseCase: MapUseCase,
    private val moveDirection: MoveDirectionUseCase,
) : ViewModel() {

    lateinit var navigationViewModel: NavigationViewModel

    fun initNavigationViewModel(owner: ViewModelStoreOwner) {
        navigationViewModel = ViewModelProvider(owner)[NavigationViewModel::class.java]
    }

    private val _robotState = MutableStateFlow<RobotState>(RobotState.IDLE)
    val robotState: StateFlow<RobotState> = _robotState

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages

    private val _navigationResult = MutableStateFlow<Boolean?>(null)
    val navigationResult: StateFlow<Boolean?> = _navigationResult

    private val _taskId = MutableStateFlow<Int?>(null)
    val taskId: StateFlow<Int?> = _taskId

    private val _isElevatorPointReached = MutableStateFlow(false)

    // Theo dõi hướng di chuyển của robot
    private val _currentJourney = MutableStateFlow<JourneyDirection>(JourneyDirection.NONE)

    enum class JourneyDirection {
        DOWN_13_TO_1,
        UP_1_TO_13,
        NONE
    }

    val positionDes = """{"x": -6.4490547, "y": -14.451439, "z": 0.0, "rotation": 126.7954562}"""

    // Định nghĩa các điểm định vị
//    companion object {
//        // Tầng 13
//        private const val FLOOR_13_ELEVATOR_POINT =
//            "x= 2.6824255f, y=-0.2290653f, z=0.0f, rotation=-0.2657993f"
//        private const val FLOOR_13_CABIN_POINT =
//            "x= -0.21469636f, y=0.006519f, z=0.0f, rotation=-0.19207564f"
//
//        // Tầng 1
//        private const val FLOOR_1_ELEVATOR_POINT =
//            "x= 2.6824255f, y=-0.2290653f, z=0.0f, rotation=-0.2657993f"
//        private const val FLOOR_1_CABIN_POINT =
//            "x= -0.21469636f, y=0.006519f, z=0.0f, rotation=-0.19207564f"
//    }

    init {
        connectWebSocket()
        listenToElevatorMessages()
    }

    // Kết nối WebSocket
    private fun connectWebSocket() {
        elevatorRepository.connect()
    }

    // Lắng nghe tin nhắn từ WebSocket
    private fun listenToElevatorMessages() {
        viewModelScope.launch {
            elevatorRepository.receiveMessages().collect { message ->
                _messages.value = _messages.value + message
                handleElevatorMessage(message)
            }
        }
    }

    // Gọi thang máy đi xuống (13->1)
    fun callElevatorDown(currentFloor: Int = 13, destinationFloor: Int = 1) {
        _currentJourney.value = JourneyDirection.DOWN_13_TO_1
        _robotState.value = RobotState.CALLING_ELEVATOR
        viewModelScope.launch {
            try {
                val response = elevatorRepository.callElevator(
                    currentFloor = currentFloor,
                    destinationFloor = destinationFloor
                )
                Log.d("Elevator", "Down Journey Response: $response")

                _robotState.value = RobotState.WAITING_FOR_ELEVATOR
                navigateToElevatorPoint()
            } catch (e: Exception) {
                Log.e("Elevator", "Error calling elevator for down journey: ${e.message}")
            }
        }
    }

    // Gọi thang máy đi lên (1->13)
    fun callElevatorUp(currentFloor: Int = 1, destinationFloor: Int = 13) {
        _currentJourney.value = JourneyDirection.UP_1_TO_13
        _robotState.value = RobotState.CALLING_ELEVATOR
        viewModelScope.launch {
            try {
                val response = elevatorRepository.callElevator(
                    currentFloor = currentFloor,
                    destinationFloor = destinationFloor
                )
                Log.d("Elevator", "Up Journey Response: $response")

                _robotState.value = RobotState.WAITING_FOR_ELEVATOR
                navigateToReturnElevatorPoint()
            } catch (e: Exception) {
                Log.e("Elevator", "Error calling elevator for up journey: ${e.message}")
            }
        }
    }

    // Xử lý tin nhắn từ WebSocket
    private suspend fun handleElevatorMessage(message: String) {
        try {
            Log.d("ElevatorMessage", "Received message: $message")
            val jsonObject = JSONObject(message)
            val event = jsonObject.getString("event")

            when (event) {
                "elevator-arrived-to-pick-up-robot" -> {
                    val data = jsonObject.getJSONObject("data")
                    if (data.has("task_id")) {
                        val taskId = data.getInt("task_id")
                        _taskId.value = taskId
                        Log.d("ElevatorMessage", "Task ID received: $taskId")

                        if (jsonObject.has("msg_id")) {
                            val msgId = jsonObject.getString("msg_id")
                            sendRobotConfirmation(msgId)
                        }
                        waitForElevatorPointAndProceed()
                    }
                }

                "elevator-arrived-to-destination" -> {
                    val data = jsonObject.getJSONObject("data")
                    if (data.has("task_id")) {
                        val msgId = jsonObject.getString("msg_id")
                        sendRobotConfirmation(msgId)

                        Log.e("iiiiii", _robotState.value.toString())

                        if (_robotState.value == RobotState.INSIDE_CABIN) {
                            _robotState.value = RobotState.MOVING_TO_EXIT_CABIN
                            navigateToPositionUseCase.setSpeed(0.2f)
                            delay(5000)
                            when (_currentJourney.value) {
                                JourneyDirection.DOWN_13_TO_1 -> {
//                                    loadMapByName("1-2")
                                    Log.e("iiiiii", "Down 1")
                                    navigateToExitCabin()
                                }

                                JourneyDirection.UP_1_TO_13 -> {
//                                    loadMapByName("12A-2")
                                    Log.e("iiiiii", "Up 13")

                                    navigateToReturnExitCabin()
//                                    navigateToDestination(positionDes)

                                }

                                JourneyDirection.NONE -> {
                                    Log.e("ElevatorMessage", "No journey direction specified")
                                }
                            }
                        }

                        else {
                            Log.e("iiiiiii", "dadadadadadadadadadada")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ElevatorMessage", "Error parsing elevator message: ${e.message}")
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
                Log.d("WebSocket iii", "Sent robot confirmation: $confirmationMessage")
            } catch (e: Exception) {
                Log.e("WebSocket iiii", "Error sending robot confirmation: ${e.message}")
            }
        }
    }

    // Gửi thông báo robot đã vào cabin
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
                Log.d("WebSocket iiii", "Sent robot in cabin message: $message")
            } catch (e: Exception) {
                Log.e("WebSocket iiii", "Error sending robot in cabin message: ${e.message}")
            }
        }
    }

    // Gửi thông báo robot đã ra khỏi cabin
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
                Log.d("WebSocket iiiii", "Sent robot exit cabin message: $message")
            } catch (e: Exception) {
                Log.e("WebSocket iiiii", "Error sending robot exit cabin message: ${e.message}")
            }
        }
    }

    // Xử lý sau khi đến điểm chờ thang máy
    private fun waitForElevatorPointAndProceed() {
        viewModelScope.launch {
            _isElevatorPointReached.collect { isReached ->
                if (isReached) {
                    _robotState.value = RobotState.MOVING_TO_CABIN
//                    delay(5000)
                    navigateToPositionUseCase.setSpeed(0.2f)
                    when (_currentJourney.value) {
                        JourneyDirection.DOWN_13_TO_1 -> navigateToCabin()
                        JourneyDirection.UP_1_TO_13 -> navigateToReturnCabin()
                        JourneyDirection.NONE -> Log.e(
                            "Navigation",
                            "No journey direction specified"
                        )
                    }
                    _isElevatorPointReached.value = false
                }
            }
        }
    }

    // Di chuyển đến điểm chờ thang máy tầng 13
    private fun navigateToElevatorPoint() {
        val position = "x= 2.6824255f, y=-0.2290653f, z=0.0f, rotation=-0.2657993f"

        viewModelScope.launch {
            try {
                _isElevatorPointReached.value = false
                val result = navigateToPositionUseCase(position)
                _navigationResult.value = result

                if (result == true) {
                    _isElevatorPointReached.value = true
                    Log.d("NavigationViewModel", "Reached floor 13 elevator point")
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error navigating to elevator point: ${e.message}")
            }
        }
    }

    // Di chuyển đến điểm chờ thang máy tầng 1
    private fun navigateToReturnElevatorPoint() {
        val position = "x= 2.6824255f, y=-0.2290653f, z=0.0f, rotation=-0.2657993f"

        viewModelScope.launch {
            try {
                _isElevatorPointReached.value = false
                val result = navigateToPositionUseCase(position)
                _navigationResult.value = result

                if (result == true) {
                    _isElevatorPointReached.value = true
                    Log.d("NavigationViewModel", "Reached floor 1 elevator point")
                }
            } catch (e: Exception) {
                Log.e(
                    "NavigationViewModel",
                    "Error navigating to floor 1 elevator point: ${e.message}"
                )
            }
        }
    }

    // Di chuyển vào cabin từ tầng 13
    private fun navigateToCabin() {
        val position = "x= -0.21469636f, y=0.006519f, z=0.0f, rotation=-0.19207564f"

        viewModelScope.launch {
            try {
                val result = navigateToPosition2UseCase(position)
                _navigationResult.value = result

                if (result == true) {
                    val currentTaskId = _taskId.value
                    Log.e("iiiii", _taskId.value.toString())
                    if (currentTaskId != null) {
                        Log.e("iiiiii", _taskId.value.toString())
                        sendRobotInCabinMessage(currentTaskId)
                        _robotState.value = RobotState.INSIDE_CABIN
                        loadMapByName("1-2")
                    }
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error navigating to cabin: ${e.message}")
            }
        }
    }

    // Di chuyển vào cabin từ tầng 1
    private fun navigateToReturnCabin() {
        val position = "x= -0.21469636f, y=0.006519f, z=0.0f, rotation=-0.19207564f"
        viewModelScope.launch {
            try {
                val result = navigateToPosition2UseCase(position)
                _navigationResult.value = result

                if (result == true) {
                    val currentTaskId = _taskId.value
                    Log.e("iiiiiiiiii", _taskId.value.toString())
                    if (currentTaskId != null) {
                        Log.e("iiiiii", _taskId.value.toString())
                        sendRobotInCabinMessage(currentTaskId)
                        _robotState.value = RobotState.INSIDE_CABIN
                        loadMapByName("12A-2")
                    }
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error navigating to return cabin: ${e.message}")
            }
        }
    }

    // Di chuyển ra khỏi cabin ở tầng 1
    private fun navigateToExitCabin() {
        val position = "x= 2.6824255f, y=-0.2290653f, z=0.0f, rotation=-0.2657993f"

        viewModelScope.launch {
            try {
                val result = navigateToPositionUseCase(position)
                _navigationResult.value = result

                if (result == true) {
                    val currentTaskId = _taskId.value
                    if (currentTaskId != null) {
                        sendRobotExitCabinMessage(currentTaskId)
                        _robotState.value = RobotState.IDLE
                        navigateToPositionUseCase.setSpeed(0.8f)
//                        elevatorRepository.disconnect()
//                        _taskId.value = null
                    }
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error navigating to exit cabin: ${e.message}")
            }
        }
    }

    // Di chuyển ra khỏi cabin ở tầng 13
    private fun navigateToReturnExitCabin() {
        val position = "x= 2.6824255f, y=-0.2290653f, z=0.0f, rotation=-0.2657993f"

        viewModelScope.launch {
            try {
                val result = navigateToPositionUseCase(position)
                _navigationResult.value = result

                if (result == true) {
                    val currentTaskId = _taskId.value
                    if (currentTaskId != null) {
                        sendRobotExitCabinMessage(currentTaskId)
                        _robotState.value = RobotState.IDLE
                        navigateToPositionUseCase.setSpeed(0.8f)
                        goHome()
//                        elevatorRepository.disconnect()
//                        _taskId.value = null
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    "NavigationViewModel",
                    "Error navigating to exit cabin on floor 13: ${e.message}"
                )
            }
        }
    }

    fun navigateToDestination(position: String) {
        viewModelScope.launch {
            try {
                val result = navigateToDestinationUseCase(position)
                _navigationResult.value = result
                Log.d("NavigationViewModel", "Navigation to destination result: $result")

                if (result == true) {
                    val currentTaskId = _taskId.value
                    if (currentTaskId != null) {
                        sendRobotExitCabinMessage(currentTaskId)
                        _robotState.value = RobotState.IDLE
//                        elevatorRepository.disconnect()
//                        _taskId.value = null
                    }
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error navigating to destination: ${e.message}")
            }
        }
    }

    // Load map theo tên
    fun loadMapByName(name: String) {
        viewModelScope.launch {
            try {
                mapUseCase.loadMapByName(name)
                Log.d("NavigationViewModel", "Map loaded: $name")
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error loading map: ${e.message}")
            }
        }
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
                Log.d("WebSocket iiii", "Sent cancel all tasks message: $message")
            } catch (e: Exception) {
                Log.e("WebSocket iiii", "Error sending cancel all tasks message: ${e.message}")
            }
        }
    }

    fun goHome(){
        viewModelScope.launch {
            moveDirection.goHome()
        }
    }
}

