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
import com.phenikaa.h1_robot_app.domain.usecase.robotdoor.RobotDoorUseCase
import com.phenikaa.h1_robot_app.presentation.features.navigation.NavigationViewModel
import com.phenikaa.h1_robot_app.state.RobotState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ElevatorViewModel @Inject constructor(
    private val elevatorRepository: ElevatorRepository,
    private val navigateToPositionUseCase: NavigateToPositionUseCase,
    private val navigateToPosition2UseCase: NavigateToPosition2UseCase,
    private val navigateToDestinationUseCase: NavigateToDestinationUseCase,
    private val naviDataSource: RobotNaviDataSource,
    private val mapUseCase: MapUseCase,
    private val moveDirection: MoveDirectionUseCase,
    private val doorControlUseCase: RobotDoorUseCase
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

    private val _selectedDoors = MutableStateFlow<Pair<Boolean, Boolean>>(false to false)
    val selectedDoors: StateFlow<Pair<Boolean, Boolean>> = _selectedDoors.asStateFlow()

    private val _selectedPoint = MutableStateFlow<String?>(null)
    val selectedPoint: StateFlow<String?> get() = _selectedPoint



    enum class JourneyDirection {
        START_TO_WORK,
        GO_HOME,
        NONE
    }

    val positionDes = """{"x": -6.4490547, "y": -14.451439, "z": 0.0, "rotation": 126.7954562}"""
    val positionOutSide = """{"x": 2.6824255, "y": -0.2290653, "z": 0.0, "rotation": -0.2657993}"""
    val positionInSide = """{"x": -0.21469636, "y": 0.006519, "z": 0.0, "rotation": -0.19207564}"""


    init {
//        connectWebSocket()
//        listenToElevatorMessages()
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

    fun selectPoint(point: String) {
        _selectedPoint.value = point
        Log.d("Elevator", "Selected point: $point")
    }

    // Gọi thang máy đi xuống (13->1)
    fun callElevatorDown(currentFloor: Int = 13, destinationFloor: Int = 1) {
        val point = _selectedPoint.value
        if(point != null){
            _currentJourney.value = JourneyDirection.START_TO_WORK
            _robotState.value = RobotState.CALLING_ELEVATOR

            // Lưu trạng thái cửa đã chọn trước khi gọi thang máy
            _selectedDoors.value = _selectedDoors.value

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
        } else {
            Log.e("Elevator", "No point selected")
        }
    }

    // Gọi thang máy đi lên (1->13)
    fun callElevatorUp(currentFloor: Int = 1, destinationFloor: Int = 13) {
        _currentJourney.value = JourneyDirection.GO_HOME
        _robotState.value = RobotState.CALLING_ELEVATOR
        viewModelScope.launch {
            try {
                val response = elevatorRepository.callElevator(
                    currentFloor = currentFloor,
                    destinationFloor = destinationFloor
                )
                Log.d("Elevator", "Up Journey Response: $response")

                _robotState.value = RobotState.WAITING_FOR_ELEVATOR
//                navigateToReturnElevatorPoint()
                navigateToElevatorPoint()
            } catch (e: Exception) {
                Log.e("Elevator", "Error calling elevator for up journey: ${e.message}")
            }
        }
    }

    // Xử lý tin nhắn từ WebSocket
    private suspend fun handleElevatorMessage(message: String) {
//        try {
//            Log.d("ElevatorMessage", "Received message: $message")
//            val jsonObject = JSONObject(message)
//            val event = jsonObject.getString("event")
//
//            when (event) {
//                "elevator-arrived-to-pick-up-robot" -> {
//                    val data = jsonObject.getJSONObject("data")
//                    if (data.has("task_id")) {
//                        val taskId = data.getInt("task_id")
//                        _taskId.value = taskId
//                        Log.d("ElevatorMessage", "Task ID received: $taskId")
//
//                        if (jsonObject.has("msg_id")) {
//                            val msgId = jsonObject.getString("msg_id")
//                            sendRobotConfirmation(msgId)
//                        }
//                        waitForElevatorPointAndProceed()
//                    }
//                }
//
//                "elevator-arrived-to-destination" -> {
//                    val data = jsonObject.getJSONObject("data")
//                    if (data.has("task_id")) {
//                        val msgId = jsonObject.getString("msg_id")
//                        sendRobotConfirmation(msgId)
//
//                        Log.e("iiiiii", _robotState.value.toString())
//
//                        if (_robotState.value == RobotState.INSIDE_CABIN) {
//                            _robotState.value = RobotState.MOVING_TO_EXIT_CABIN
//                            navigateToPositionUseCase.setSpeed(0.3f)
//                            delay(5000)
//                            when (_currentJourney.value) {
//                                JourneyDirection.START_TO_WORK -> {
//                                    loadMapByName("1-2")
//                                    Log.e("iiiiii", "Down 1")
//                                    navigateToExitCabin()
//                                }
//
//                                JourneyDirection.GO_HOME -> {
//                                    loadMapByName("12A-2")
//                                    Log.e("iiiiii", "Up 13")
//
////                                    navigateToReturnExitCabin()
//                                    navigateToExitCabin()
////                                    navigateToDestination(positionDes)
//
//                                }
//
//                                JourneyDirection.NONE -> {
//                                    Log.e("ElevatorMessage", "No journey direction specified")
//                                }
//                            }
//                        }
//
//                        else {
//                            Log.e("iiiiiii", "dadadadadadadadadadada")
//                        }
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("ElevatorMessage", "Error parsing elevator message: ${e.message}")
//        }
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
                    navigateToPositionUseCase.setSpeed(0.3f)
                    when (_currentJourney.value) {
                        JourneyDirection.START_TO_WORK -> navigateToCabin()
                        JourneyDirection.GO_HOME -> navigateToCabin()
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
//        val position = "x= 2.6824255f, y=-0.2290653f, z=0.0f, rotation=-0.2657993f"

        viewModelScope.launch {
            try {
                _isElevatorPointReached.value = false
//                val result = navigateToPositionUseCase(position)
                val result = navigateToDestinationUseCase(positionOutSide)
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

    // Di chuyển vào cabin từ tầng 13
    private fun navigateToCabin() {
//        val position = "x= -0.21469636f, y=0.006519f, z=0.0f, rotation=-0.19207564f"

        viewModelScope.launch {
            try {
//                val result = navigateToPosition2UseCase(position)
                val result = navigateToDestinationUseCase(positionInSide)
                _navigationResult.value = result

                if (result == true) {
                    val currentTaskId = _taskId.value
                    Log.e("iiiii", _taskId.value.toString())
                    if (currentTaskId != null) {
                        Log.e("iiiiii", _taskId.value.toString())
                        sendRobotInCabinMessage(currentTaskId)
                        _robotState.value = RobotState.INSIDE_CABIN
//                        loadMapByName("1-2")
                    }
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error navigating to cabin: ${e.message}")
            }
        }
    }

    // Di chuyển ra khỏi cabin ở tầng 1
    private fun navigateToExitCabin() {
        val position = "x= 2.6824255f, y=-0.2290653f, z=0.0f, rotation=-0.2657993f"

        viewModelScope.launch {
            try {
//                val result = navigateToPositionUseCase(position)
                val result = navigateToDestinationUseCase(positionOutSide)
                _navigationResult.value = result

                if (result == true) {
                    val currentTaskId = _taskId.value
                    if (currentTaskId != null) {
                        sendRobotExitCabinMessage(currentTaskId)
                        _robotState.value = RobotState.IDLE
                        navigateToPositionUseCase.setSpeed(0.8f)
                        if (_currentJourney.value == JourneyDirection.GO_HOME) {
                            goHome()
                            Log.d("NavigationViewModel", "Robot is going home after exiting cabin at floor 13")
                        }
//                        elevatorRepository.disconnect()
//                        _taskId.value = null
                    }
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error navigating to exit cabin: ${e.message}")
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

    fun selectDoor(door1: Boolean, door2: Boolean) {
        _selectedDoors.value = Pair(door1, door2)
    }

    fun openSelectedDoors() {
        val (door1, door2) = _selectedDoors.value

        viewModelScope.launch {
            if (door1) doorControlUseCase.openOneFloorDoor()
            if (door2) doorControlUseCase.openTwoFloorDoor()
        }
    }

    fun closeDoorsAndMoveUp() {
        val (door1, door2) = _selectedDoors.value

        viewModelScope.launch {
            if (door1) doorControlUseCase.closeOneFloorDoor()
            if (door2) doorControlUseCase.closeTwoFloorDoor()

            delay(2000)

            callElevatorUp()
        }
    }

}

