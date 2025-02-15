package com.phenikaa.h1_robot_app.presentation.features.elevator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.phenikaa.h1_robot_app.data.api.ApiPointClient
import com.phenikaa.h1_robot_app.data.api.PointsApiService
import com.phenikaa.h1_robot_app.data.model.Point
import com.phenikaa.h1_robot_app.data.repository.ElevatorRepository
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToDestinationUseCase
import com.phenikaa.h1_robot_app.domain.usecase.robotdoor.RobotDoorUseCase
import com.phenikaa.h1_robot_app.presentation.features.navigation.NavigationViewModel
import com.phenikaa.h1_robot_app.state.RobotState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ElevatorGoHomeViewModel @Inject constructor(
    private val navigateToDestinationUseCase: NavigateToDestinationUseCase,
    private val elevatorRepository: ElevatorRepository,
    private val doorControlUseCase: RobotDoorUseCase

    ) : ViewModel() {

    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points: StateFlow<List<Point>> get() = _points

    lateinit var navigationViewModel: NavigationViewModel

    fun initNavigationViewModel(owner: ViewModelStoreOwner) {
        navigationViewModel = ViewModelProvider(owner)[NavigationViewModel::class.java]
    }
    private val _robotState = MutableStateFlow<RobotState>(RobotState.IDLE)
    val robotState: StateFlow<RobotState> = _robotState

    private var taskId: Int? = null
    private var currentStepIndex = 0
    private var navigationSteps: List<JSONObject> = emptyList()
    private var stageId: Int? = null

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> get() = _currentPage

    private val _perPage = MutableStateFlow(10)
    val perPage: StateFlow<Int> get() = _perPage

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> get() = _totalPages

    private val _selectedPoint = MutableStateFlow<String?>(null)
    val selectedPoint: StateFlow<String?> get() = _selectedPoint

    private var selectedPointId: Int? = null

    private val _selectedDoors = MutableStateFlow<Pair<Boolean, Boolean>>(false to false)
    val selectedDoors: StateFlow<Pair<Boolean, Boolean>> = _selectedDoors.asStateFlow()

    private var _routeAnalyzeData: JSONObject? = null



    init {
//        connectWebSocket()
//        listenToWebSocket()
    }

    fun loadPoints(page: Int = 1) {
        viewModelScope.launch {
            try {
                val response = ApiPointClient.apiService.getPoints(page, _perPage.value)

                if (response.data.points.isNotEmpty()) {
                    _points.value = response.data.points
                    _currentPage.value = page
                    _totalPages.value = maxOf((response.data.total / _perPage.value), 1)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Kết nối WebSocket
    private fun connectWebSocket() {
        elevatorRepository.connect()
    }

    private fun listenToWebSocket() {
        viewModelScope.launch {
            elevatorRepository.receiveMessages().collect { message ->
                handleWebSocketMessage(message)
                Log.e("ddddd", "hhhhh")
            }
        }
    }

    private fun handleWebSocketMessage(message: String) {
//        Log.e("ddd", "iiiiiii")
//        val jsonObject = JSONObject(message)
//        when (jsonObject.getString("event")) {
//            "route_analyze" -> handleRouteAnalyze(jsonObject)
//            "task_step_confirmed" -> handleTaskStepConfirmed(jsonObject)
//            "stage_finished" -> handleStageFinished(jsonObject)
//        }
    }

    private fun handleRouteAnalyze(json: JSONObject) {
        Log.e("ElevatorViewModel", "Processing route_analyze")

        if (json.getString("status") == "success") {
            _routeAnalyzeData = json // Lưu lại toàn bộ dữ liệu của message

            val data = json.getJSONObject("data")
            taskId = data.getInt("task_id")
            val stages = data.getJSONArray("data")

            if (stages.length() > 0) {
                startStage(stages, 0)
            }
        }
    }



    private fun handleTaskStepConfirmed(json: JSONObject) {
        val confirmationCode = json.getJSONObject("data").getString("confirmation_code")
        Log.d("ElevatorViewModel", "Step confirmed: $confirmationCode")

        currentStepIndex++
        if (currentStepIndex < navigationSteps.size) {
            executeNextStep()
        } else {
            Log.d("ElevatorViewModel", "Stage completed, sending stage_finished event")
            sendStageFinished()
        }
    }

    private fun handleStageFinished(json: JSONObject) {
        Log.d("ElevatorViewModel", "Stage completed, checking for next stage...")

        val finishedStageId = json.getJSONObject("data").getInt("stage_id")

        // Kiểm tra nếu còn stage trong route_analyze
        val stages = _routeAnalyzeData?.getJSONObject("data")?.getJSONArray("data") ?: return

        val nextStageIndex = findStageIndexById(stages, finishedStageId) + 1
        if (nextStageIndex < stages.length()) {
            Log.d("ElevatorViewModel", "Starting next stage...")

            startStage(stages, nextStageIndex) // Chuyển sang stage tiếp theo
        } else {
            Log.d("ElevatorViewModel", "All stages completed.")
        }
    }

    private fun startStage(stages: JSONArray, stageIndex: Int) {
        val stage = stages.getJSONObject(stageIndex)
        stageId = stage.getInt("stage_id")
        navigationSteps = parseNavigationSteps(stage.getJSONArray("navigation_steps"))

        Log.d("ElevatorViewModel", "Starting Stage $stageId with ${navigationSteps.size} steps")

        currentStepIndex = 0
        executeNextStep() // Thực hiện bước đầu tiên của stage mới
    }

    private fun findStageIndexById(stages: JSONArray, stageId: Int): Int {
        for (i in 0 until stages.length()) {
            if (stages.getJSONObject(i).getInt("stage_id") == stageId) {
                return i
            }
        }
        return -1
    }



    private fun parseNavigationSteps(jsonArray: JSONArray): List<JSONObject> {
        return (0 until jsonArray.length()).map { index -> jsonArray.getJSONObject(index) }
    }

    private fun executeNextStep() {
        if (currentStepIndex >= navigationSteps.size) return

        val step = navigationSteps[currentStepIndex]
        val action = step.getString("action")
        val confirmationCode = step.getString("confirmation_code")

        Log.d("ElevatorViewModel", "Executing action: $action")

        viewModelScope.launch {
            when (action) {
                "MoveToTarget" -> {
                    val pose = step.getJSONObject("pose")
                    val position = """{"x": ${pose.getDouble("x")}, "y": ${pose.getDouble("y")}, "z": "0.0", "rotation": ${pose.getDouble("rotation")}}"""
                    Log.d("ElevatorViewModel", "Moving to target: $position")

                    val result = navigateToDestinationUseCase(position)
                    if (result) {
                        sendTaskStepConfirmed(confirmationCode)
                    }
                }
                "CallLift" -> {
                    Log.d("ElevatorViewModel", "Calling elevator")
                    delay(5000)
                    sendTaskStepConfirmed(confirmationCode)
                }
                "SelectFloor" -> {
                    Log.d("ElevatorViewModel", "Selecting floor")
                    delay(5000)
                    sendTaskStepConfirmed(confirmationCode)
                }
                "ExitLift" -> {
                    Log.d("ElevatorViewModel", "Exiting lift")
                    delay(5000)
                    sendTaskStepConfirmed(confirmationCode)
                }
            }
        }
    }


    private fun sendTaskStepConfirmed(confirmationCode: String) {
        val message = JSONObject().apply {
            put("event", "task_step_confirmed")
            put("data", confirmationCode)
        }.toString()

        elevatorRepository.sendMessage(message)
    }

    private fun sendStageFinished() {
        if (stageId == null) return

        val message = JSONObject().apply {
            put("event", "stage_finished")
            put("data", JSONObject().apply {
                put("stage_id", stageId)
                put("status", 3)
            })
        }.toString()

        elevatorRepository.sendMessage(message)
    }

    private fun loadMapForDestinationFloor(step: JSONObject) {
        val destinationFloor = step.getInt("destination_floor")
        val mapName = when (destinationFloor) {
            1 -> "1-2"
            13 -> "12A-2"
            else -> return
        }
        Log.d("ElevatorViewModel", "Loading map: $mapName")
    }

    fun requestRoute(destination: Int) {
        val message = JSONObject().apply {
            put("event", "route_analyze")
            put("data", JSONArray().put(destination))
        }.toString()

        elevatorRepository.sendMessage(message)
    }

    fun disconnectWebSocket() {
        elevatorRepository.disconnect()
    }

    fun selectPoint(pointName: String, pointId: Int) {
        _selectedPoint.value = pointName
        selectedPointId = pointId
        Log.d("Elevator", "Selected point: $pointName")
    }

    fun getSelectedPointId(): Int? {
        return selectedPointId
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
        if (currentStepIndex >= navigationSteps.size) return

        val step = navigationSteps[currentStepIndex]
        val action = step.getString("action")
        val confirmationCode = step.getString("confirmation_code")

        Log.d("ElevatorViewModel", "Executing action: $action")
        val (door1, door2) = _selectedDoors.value

        viewModelScope.launch {
            if (door1) doorControlUseCase.closeOneFloorDoor()
            if (door2) doorControlUseCase.closeTwoFloorDoor()

            delay(2000)

            sendTaskStepConfirmed(confirmationCode)
        }
    }
}
