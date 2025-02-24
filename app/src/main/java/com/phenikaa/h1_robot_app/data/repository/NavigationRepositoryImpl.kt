package com.phenikaa.h1_robot_app.data.repository

import android.util.Log
import com.csjbot.coshandler.listener.OnMapListener
import com.phenikaa.h1_robot_app.data.datasource.robot.RobotNaviDataSource
import com.phenikaa.h1_robot_app.data.model.RosPosition
import com.phenikaa.h1_robot_app.domain.entity.Position
import com.phenikaa.h1_robot_app.domain.repository.NavigationRepository
import javax.inject.Inject


class NavigationRepositoryImpl @Inject constructor(
    private val naviDataSource: RobotNaviDataSource
) : NavigationRepository {

    override suspend fun getCurrentPosition(): Position {
        Log.d("NavigationRepositoryImpl", "Fetching current position")
        return naviDataSource.getCurrentPosition().toDomainModel()
    }

//    override suspend fun navigateToPosition(position: RosPosition): Flow<NavigationState> = flow {
//        emit(NavigationState.Navigating)
//        try {
//            val rosPosition = RosPosition.fromDomainModel(position)
//            val isReachable = naviDataSource.isDestinationReachable(rosPosition)
//
//            if (!isReachable) {
//                emit(NavigationState.Error("Destination is not reachable"))
//                return@flow
//            }
//
//            val success = naviDataSource.navigateToPosition(rosPosition)
//            if (success) {
//                emit(NavigationState.Completed)
//            } else {
//                emit(NavigationState.Error("Navigation failed"))
//            }
//        } catch (e: Exception) {
//            emit(NavigationState.Error(e.message ?: "Unknown error"))
//        }
//    }

    override suspend fun navigateToPosition(position: RosPosition): Boolean {
        return naviDataSource.navigateToPosition(position)
    }

    override suspend fun navigateToPosition2(position: String): Boolean {
        return naviDataSource.navigateToPosition2(position)
    }
    override suspend fun navigateToDestination(position: String): Boolean {
        return naviDataSource.navigateToDestination(position)
    }

    override suspend fun cancelNavigation() {
        naviDataSource.cancelNavigation(null)
    }

    override suspend fun moveDirection(direction: Int) {
        naviDataSource.moveDirection(direction)
    }

    override suspend fun moveBySerial(direction: Int){
        naviDataSource.moveBySerial(direction)
    }

    override suspend fun moveSerial(linear: Int, angular: Int) {
        naviDataSource.moveSerial(linear, angular)
    }

    override suspend fun goAngle(angle: Int) {
        naviDataSource.goAngle(angle)
    }
    override suspend fun moveAngle(angle: Int){
        naviDataSource.moveAngle(angle)
    }

    override suspend fun setSpeed(speed: Float) {
        naviDataSource.setSpeed(speed)
    }

    override suspend fun getSpeed(): Float {
        return naviDataSource.getSpeed()
    }

    override suspend fun saveMap(name: String): Result<Unit> {
        TODO("Not yet implemented")
    }

//    override suspend fun loadMap(name: String): Result<Unit> {
//        TODO("Not yet implemented")
//    }

    override suspend fun getMapList(): List<String> {
        return naviDataSource.getMapList()
    }

//    override suspend fun goHome(): Flow<NavigationState> = flow {
//        emit(NavigationState.Navigating) // Phát trạng thái bắt đầu điều hướng
//
//        try {
//            val success = naviDataSource.goHome() // Gọi hàm từ DataSource
//            if (success) {
//                emit(NavigationState.Completed) // Thành công
//            } else {
//                emit(NavigationState.Error("Failed to return to charging pile")) // Thất bại
//            }
//        } catch (e: Exception) {
//            emit(NavigationState.Error(e.message ?: "Unknown error")) // Báo lỗi
//        }
//    }

    override suspend fun goHome(): Boolean {
        return naviDataSource.goHome()
    }

//    override suspend fun checkDestinationReachable(position: Position): Boolean {
//        val rosPosition = RosPosition.fromDomainModel(position)
//        return naviDataSource.isDestinationReachable(rosPosition)
//    }

    override suspend fun loadMap() {
       naviDataSource.loadMap()
    }

    override suspend fun loadMap(name: String) {
        naviDataSource.loadMap(name)
    }

    override suspend fun loadMap(name: String, listener: OnMapListener) {
        naviDataSource.loadMap(name, listener)
    }

    override suspend fun loadMap(
        name: String,
        x: Float,
        y: Float,
        rotation: Float,
//        listener: OnMapListener?
    ) {
        naviDataSource.loadMap(name, x, y, rotation)
    }

    override suspend fun loadMapWithListener(
        name: String,
        x: Float,
        y: Float,
        rotation: Float,
        listener: OnMapListener
    ) {
        naviDataSource.loadMap(name, x, y, rotation, listener)
    }

}