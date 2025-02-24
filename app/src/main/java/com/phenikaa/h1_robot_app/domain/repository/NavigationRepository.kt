package com.phenikaa.h1_robot_app.domain.repository

import com.csjbot.coshandler.listener.OnMapListener
import com.phenikaa.h1_robot_app.data.model.RosPosition
import com.phenikaa.h1_robot_app.domain.entity.Position

interface NavigationRepository {
    suspend fun getCurrentPosition(): Position
    suspend fun navigateToPosition(position: RosPosition): Boolean
    suspend fun navigateToPosition2(position: String): Boolean
    suspend fun navigateToDestination(position: String): Boolean
    suspend fun cancelNavigation()
    suspend fun moveDirection(direction: Int)
    suspend fun moveBySerial(direction: Int)
    suspend fun moveSerial(linear: Int, angular: Int)

    suspend fun goAngle(angle: Int)
    suspend fun moveAngle(angle: Int)

    suspend fun setSpeed(speed: Float)
    suspend fun getSpeed(): Float
    suspend fun saveMap(name: String): Result<Unit>
//    suspend fun loadMap(name: String): Result<Unit>
    suspend fun getMapList(): List<String>
//    suspend fun goHome(): Flow<NavigationState>

    suspend fun goHome(): Boolean
//    suspend fun checkDestinationReachable(position: Position): Boolean

    suspend fun loadMap()
    suspend fun loadMap(name: String)
    suspend fun loadMap(name: String, listener: OnMapListener)
    suspend fun loadMap(name: String, x: Float, y: Float, rotation: Float)
    suspend fun loadMapWithListener(name: String, x: Float, y: Float, rotation: Float, listener: OnMapListener)


}