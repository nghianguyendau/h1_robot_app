package com.phenikaa.h1_robot_app.domain.repository

import com.csjbot.coshandler.listener.OnDoubleDoorStateListener

interface RobotDoorRepository {
    suspend fun openDoor(listener: OnDoubleDoorStateListener)
    suspend fun closeDoor(listener: OnDoubleDoorStateListener)
    suspend fun openOneFloorDoor()
    suspend fun openTwoFloorDoor()
    suspend fun closeOneFloorDoor()
    suspend fun closeTwoFloorDoor()
}