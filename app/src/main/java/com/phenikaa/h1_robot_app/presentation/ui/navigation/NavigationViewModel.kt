package com.phenikaa.h1_robot_app.presentation.ui.navigation

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.csjbot.coshandler.core.CsjRobot
import com.csjbot.coshandler.listener.OnMapListener
import com.csjbot.coshandler.listener.OnRobotStateListener
import com.phenikaa.h1_robot_app.R
import com.phenikaa.h1_robot_app.data.source.robot.RobotNaviDataSource
import com.phenikaa.h1_robot_app.data.model.RosPosition
import com.phenikaa.h1_robot_app.domain.entity.Position
import com.phenikaa.h1_robot_app.domain.entity.NavigationState
import com.phenikaa.h1_robot_app.domain.usecase.navigation.GetCurrentPositionUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.MapUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.MoveDirectionUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToDestinationUseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToPosition2UseCase
import com.phenikaa.h1_robot_app.domain.usecase.navigation.NavigateToPositionUseCase
import com.phenikaa.h1_robot_app.domain.usecase.robotdoor.RobotDoorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val application: Application,
    private val getCurrentPositionUseCase: GetCurrentPositionUseCase,
    private val navigateToPositionUseCase: NavigateToPositionUseCase,
    private val navigateToPosition2UseCase: NavigateToPosition2UseCase,
    private val navigateToDestinationUseCase: NavigateToDestinationUseCase,
    private val moveDirection: MoveDirectionUseCase,
    private val naviDataSource: RobotNaviDataSource,
    private val mapUseCase: MapUseCase,
    private val doorControlUseCase: RobotDoorUseCase


) : AndroidViewModel(application) {
    private var movementJob: Job? = null

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.Idle)
    val navigationState: StateFlow<NavigationState> = _navigationState

    private val _currentPosition = MutableStateFlow<Position?>(null)
    val currentPosition: StateFlow<Position?> = _currentPosition

    private val _currentSpeed = MutableStateFlow<Float?>(null)
    val currentSpeed: StateFlow<Float?> get() = _currentSpeed

    private val _mapList = MutableStateFlow<List<String>>(emptyList())
    val mapList: StateFlow<List<String>> get() = _mapList

    private val _mapListError = MutableStateFlow<String?>(null)
    val mapListError: StateFlow<String?> get() = _mapListError

    private val _selectedMap = MutableStateFlow<String?>(null)
    val selectedMap: StateFlow<String?> get() = _selectedMap
    val selectedFloors: StateFlow<String?> get() = _selectedMap

    private val _doorState = MutableStateFlow<Pair<Int, Int>?>(null)
    val doorState: StateFlow<Pair<Int, Int>?> = _doorState.asStateFlow()

    private val _logFilePath = MutableStateFlow<String?>(null)
    val logFilePath: StateFlow<String?> = _logFilePath.asStateFlow()
    private val _visitedPoints = mutableListOf<String>()
    private var startTime: Long = 0
//    private var startBattery: Int = 100 // Mặc định
//    private var endBattery: Int = 100 // Mặc định

    private var mediaPlayer: MediaPlayer? = null

    init {
        viewModelScope.launch {
            val logFile = File(getApplication<Application>().filesDir, "robot_movement_log.txt")
            if (logFile.exists()) {
                _logFilePath.value = logFile.absolutePath
            }
        }
    }

    fun startMusic() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(application, R.raw.music_test)
        mediaPlayer?.isLooping = true // Nhạc lặp lại liên tục
        mediaPlayer?.start()
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

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


//    fun navigateToPosition(position: RosPosition) {
//        viewModelScope.launch {
////            navigateToPositionUseCase(position).collect { state ->
////                _navigationState.value = state
////            }
////            navigateToPositionUseCase.navigateToPosition(position)
//            val result = navigateToPositionUseCase(position)
//            _navigationResult.value = result
//        }
//    }

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

    fun saveCurrentPosition(name: String) {
        viewModelScope.launch {
            try {
                val currentPosition = naviDataSource.getCurrentPosition()

                val rosPosition = RosPosition(
                    poseName = name,
                    pos = RosPosition.PosBean(
                        x = currentPosition.pos.x,
                        y = currentPosition.pos.y,
                        z = currentPosition.pos.z,
                        rotation = currentPosition.pos.rotation
                    )
                )
                _savedPosition.value = rosPosition
                Log.d("NavigationViewModel", "Saved position: $rosPosition")
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error saving position: ${e.message}")
            }
        }
    }

    fun navigateToSavedPosition2() {
//        val position = "x= 15.971256f, y=-4.607306f, z=0.0f, rotation=-87.812195f"
//        val position = "x= -13.873219f, y=2.7112331f, z=0.0f, rotation=87.985886f"
        val position = "x= -6.2440734f, y=-14.355196f, z=0.0f, rotation=119.73541f"

        if (position == null) {
            Log.e("NavigationViewModel", "No position saved to navigate to")
            return
        }

        viewModelScope.launch {
            val result = navigateToPosition2UseCase(position)
            _navigationResult.value = result
//            navigateToPositionUseCase.navigateToPosition(position)
        }
    }

    fun navigateToSavedPosition() {
        val position = _savedPosition.value
        if (position == null) {
            Log.e("NavigationViewModel", "No position saved to navigate to")
            return
        }

        viewModelScope.launch {
            // Lấy thời gian bắt đầu
            startTime = System.currentTimeMillis()

            // Lấy phần trăm pin ban đầu
            val startBattery = fetchBatteryLevelSync()

            // Lưu điểm đã đi
            _visitedPoints.add(position.poseName ?: "Unknown Point")

            // Thực hiện điều hướng
            val result = navigateToPositionUseCase(position)
            _navigationResult.value = result

            if (result == true) {
                // Lấy thời gian kết thúc
                val endTime = System.currentTimeMillis()

                // Lấy phần trăm pin khi đến nơi
                val endBattery = fetchBatteryLevelSync()

                // Ghi log
                val logText = """
                Robot bắt đầu di chuyển lúc: ${SimpleDateFormat("HH:mm:ss").format(Date(startTime))}
                Robot đến nơi lúc: ${SimpleDateFormat("HH:mm:ss").format(Date(endTime))}
                Điểm đã đi qua: ${_visitedPoints.joinToString(", ")}
                Pin bắt đầu: $startBattery%
                Pin kết thúc: $endBattery%
            """.trimIndent()

                logToFile(logText)
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
                    // Khi điều hướng thành công, gọi hàm loadMapToPosition
                    val mapName = "1-2" // Tên map cố định
//                    val x =  1.007645f // Giá trị x cố định
//                    val y = 0.9554419f // Giá trị y cố định
//                    val rotation = 36.04042f // Giá trị rotation cố định
//                    val x =  -13.975249f
//                    val y = -0.41427234f
//                    val rotation = 93.914665f
                    Log.e("Hello", "DHDDDDD")
//                    loadMapToPosition(mapName, x, y, rotation)
//                    loadMapToPositionWithListener(mapName, x, y, rotation, listener)
                    loadMapByName(mapName)
                    Log.e("Hi", "Wwwqwqwq")

//                    loadMapByName(mapName)
                    Log.d(
                        "NavigationViewModel",
                        "Map $mapName loaded after successful navigation to destination"
                    )
                }
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error navigating to destination: ${e.message}")
            }
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

    //Map
    fun loadDefaultMap(){
        viewModelScope.launch {
            mapUseCase.loadDefaultMap()
            Log.d("NavigationViewModel", "Default map loaded")
        }
    }

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


    fun loadMapToPositionWithListener(name: String, x: Float, y: Float, rotation: Float, listener: OnMapListener){
        viewModelScope.launch {
            // Lưu Locale gốc
            val originalLocale = Locale.getDefault()

            // Tạm thời ép hệ thống dùng Locale.US
            Locale.setDefault(Locale.US)

            mapUseCase.loadMapToPositionWithListener(name, x, y, rotation, listener)
            Log.d("NavigationViewModel", "Map loaded: $name at ($x, $y, $rotation)")

            delay(5000)
            Locale.setDefault(originalLocale)

        }
    }

    fun fetchMapList() {
        viewModelScope.launch {
            try {
                val maps = mapUseCase()
                _mapList.value = maps
            } catch (e: Exception) {
                _mapListError.value = e.message
            }
        }
    }

    fun selectMap(mapName: String) {
        _selectedMap.value = mapName
        Log.d("NavigationViewModel", "Selected map: $mapName")
    }

    fun loadSelectedMap() {
        val mapName = _selectedMap.value
        if (mapName.isNullOrEmpty()) {
            Log.e("NavigationViewModel", "No map selected to load")
            return
        }

        viewModelScope.launch {
            try {
//                mapUseCase.loadMapByName(mapName)
//                Log.d("NavigationViewModel", "Map loaded: $mapName")

//                delay(3000)
                // Lưu Locale gốc
                val originalLocale = Locale.getDefault()

                // Tạm thời ép hệ thống dùng Locale.US
                Locale.setDefault(Locale.US)

                val x = -13.975249f
                val y = -0.41427234f
                val rotation = 93.914665f

                // Gọi hàm load map với vị trí chính xác
                mapUseCase.loadMapToPosition(mapName, x, y, rotation)

                Log.d("NavigationViewModel", "Map loaded successfully: $mapName at ($x, $y, $rotation)")

//                delay(8000)
                // Khôi phục Locale gốc sau khi load xong
                Locale.setDefault(originalLocale)

            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Error loading map: ${e.message}")
            }
        }
    }


    fun openDoor() {
        Log.d("ViewModel", "openDoor() called")
        viewModelScope.launch {
            doorControlUseCase.openDoor { i1, i2 ->
                Log.d("ViewModel", "openDoor() result: state1=$i1, state2=$i2")
                _doorState.value = Pair(i1, i2)
            }
            launch { openOneFloorDoor() }
            launch { openTwoFloorDoor() }
        }
    }

    fun closeDoor() {
        viewModelScope.launch {
            doorControlUseCase.closeDoor { i1, i2 -> _doorState.value = Pair(i1, i2) }
            launch { closeOneFloorDoor() }
            launch { closeTwoFloorDoor() }
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

    private suspend fun fetchBatteryLevelSync(): Int {
        return suspendCoroutine { continuation ->
            var isResumed = false // Cờ để đảm bảo chỉ gọi resume() một lần

            CsjRobot.getInstance().getState().getBattery(object : OnRobotStateListener {
                override fun getBattery(battery: Int) {
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(battery)
                    }
                }

                override fun getCharge(charge: Int) {
                }
            })
        }
    }


    fun logToFile(logText: String) {
        viewModelScope.launch {
            try {
                val logFile = File(getApplication<Application>().filesDir, "robot_movement_log.txt")
                logFile.appendText("$logText\n")

                // Lưu đường dẫn file để sử dụng sau này
                _logFilePath.value = logFile.absolutePath

                Log.d("NavigationViewModel", "Log ghi vào file: $logText")
            } catch (e: Exception) {
                Log.e("NavigationViewModel", "Lỗi khi ghi log: ${e.message}")
            }
        }
    }




}
