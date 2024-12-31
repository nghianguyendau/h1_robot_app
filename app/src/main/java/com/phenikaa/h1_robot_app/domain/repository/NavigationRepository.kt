package com.phenikaa.h1_robot_app.domain.repository

import com.phenikaa.h1_robot_app.domain.model.Position
import com.phenikaa.h1_robot_app.domain.model.NavigationState
import com.phenikaa.h1_robot_app.domain.model.MapState
import kotlinx.coroutines.flow.Flow

interface NavigationRepository {
    suspend fun getCurrentPosition(): Position
    suspend fun navigateToPosition(position: Position): Flow<NavigationState>
    suspend fun cancelNavigation()
    suspend fun moveDirection(direction: Int)
    suspend fun setSpeed(speed: Float)
    suspend fun getSpeed(): Float
    suspend fun saveMap(name: String): Result<Unit>
    suspend fun loadMap(name: String): Result<Unit>
    suspend fun getMapList(): Flow<MapState>
    suspend fun goHome(): Flow<NavigationState>
    suspend fun checkDestinationReachable(position: Position): Boolean
}