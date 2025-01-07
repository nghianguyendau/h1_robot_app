package com.phenikaa.h1_robot_app.domain.usecase.robotdoor

import com.csjbot.coshandler.listener.OnDoubleDoorStateListener
import com.phenikaa.h1_robot_app.domain.repository.RobotDoorRepository
import javax.inject.Inject

class RobotDoorUseCase @Inject constructor(
    private val robotRepository: RobotDoorRepository
) {
    suspend fun openDoor(listener: OnDoubleDoorStateListener) =
        robotRepository.openDoor(listener)

    suspend fun closeDoor(listener: OnDoubleDoorStateListener) =
        robotRepository.closeDoor(listener)

    suspend fun openOneFloorDoor() = robotRepository.openOneFloorDoor()

    suspend fun openTwoFloorDoor() = robotRepository.openTwoFloorDoor()

    suspend fun closeOneFloorDoor() = robotRepository.closeOneFloorDoor()

    suspend fun closeTwoFloorDoor() = robotRepository.closeTwoFloorDoor()
}