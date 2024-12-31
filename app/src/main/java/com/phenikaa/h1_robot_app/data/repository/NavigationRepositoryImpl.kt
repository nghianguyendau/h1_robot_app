package com.phenikaa.h1_robot_app.data.repository

import android.util.Log
import com.phenikaa.h1_robot_app.data.datasource.robot.RobotNaviDataSource
import com.phenikaa.h1_robot_app.data.model.RosPosition
import com.phenikaa.h1_robot_app.domain.model.MapState
import com.phenikaa.h1_robot_app.domain.model.Position
import com.phenikaa.h1_robot_app.domain.model.NavigationState
import com.phenikaa.h1_robot_app.domain.repository.NavigationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class NavigationRepositoryImpl @Inject constructor(
    private val naviDataSource: RobotNaviDataSource
) : NavigationRepository {

    override suspend fun getCurrentPosition(): Position {
        Log.d("NavigationRepositoryImpl", "Fetching current position")
        return naviDataSource.getCurrentPosition().toDomainModel()
    }

    override suspend fun navigateToPosition(position: Position): Flow<NavigationState> = flow {
        emit(NavigationState.Navigating)
        try {
            val rosPosition = RosPosition.fromDomainModel(position)
            val isReachable = naviDataSource.isDestinationReachable(rosPosition)

            if (!isReachable) {
                emit(NavigationState.Error("Destination is not reachable"))
                return@flow
            }

            val success = naviDataSource.navigateToPosition(rosPosition)
            if (success) {
                emit(NavigationState.Completed)
            } else {
                emit(NavigationState.Error("Navigation failed"))
            }
        } catch (e: Exception) {
            emit(NavigationState.Error(e.message ?: "Unknown error"))
        }
    }

    override suspend fun cancelNavigation() {
        naviDataSource.cancelNavigation()
    }

    override suspend fun moveDirection(direction: Int) {
        naviDataSource.moveDirection(direction)
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

    override suspend fun loadMap(name: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getMapList(): Flow<MapState> {
        TODO("Not yet implemented")
    }

    override suspend fun goHome(): Flow<NavigationState> {
        TODO("Not yet implemented")
    }

    override suspend fun checkDestinationReachable(position: Position): Boolean {
        val rosPosition = RosPosition.fromDomainModel(position)
        return naviDataSource.isDestinationReachable(rosPosition)
    }
}