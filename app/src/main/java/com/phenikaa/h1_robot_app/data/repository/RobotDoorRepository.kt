package com.phenikaa.h1_robot_app.data.repository

import android.util.Log
import com.csjbot.coshandler.core.CsjRobot
import com.csjbot.coshandler.listener.OnDoubleDoorStateListener
import com.phenikaa.h1_robot_app.domain.repository.RobotDoorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RobotDoorRepositoryImpl @Inject constructor() : RobotDoorRepository {

    override suspend fun openDoor(listener: OnDoubleDoorStateListener) {
        withContext(Dispatchers.IO) {
            try {
                CsjRobot.getInstance().getAction().openDoubleDoor(object : OnDoubleDoorStateListener {
                    override fun onDoorState(state1: Int, state2: Int) {
                        Log.d("RobotDoor", "openDoor state1=$state1, state2=$state2")
                        listener.onDoorState(state1, state2)
                    }
                })
            } catch (e: Exception) {
                Log.e("RobotDoorRepositoryImpl", "openDoor: ${e.message}")
            }
        }
    }

    override suspend fun closeDoor(listener: OnDoubleDoorStateListener) {
        withContext(Dispatchers.IO) {
            try {
                CsjRobot.getInstance().getAction().closeDoubleDoor(object : OnDoubleDoorStateListener {
                    override fun onDoorState(state1: Int, state2: Int) {
                        Log.d("RobotDoor", "closeDoor state1=$state1, state2=$state2")
                        listener.onDoorState(state1, state2)
                    }
                })
            } catch (e: Exception) {
                Log.e("RobotDoorRepositoryImpl", "closeDoor: ${e.message}")
            }
        }
    }

    override suspend fun openOneFloorDoor() {
        withContext(Dispatchers.IO) {
            repeat(3) {
                try {
                    CsjRobot.getInstance().getState().openOneFloorDoor()
                    delay(200)
                } catch (e: Exception) {
                    Log.e("RobotDoorRepositoryImpl", "openOneFloorDoor: ${e.message}")
                }
            }
        }
    }

    override suspend fun openTwoFloorDoor() {
        withContext(Dispatchers.IO) {
            repeat(3) {
                try {
                    CsjRobot.getInstance().getState().openTwoFloorDoor()
                    delay(200)
                } catch (e: Exception) {
                    Log.e("RobotDoorRepositoryImpl", "openTwoFloorDoor: ${e.message}")
                }
            }
        }
    }

    override suspend fun closeOneFloorDoor() {
        withContext(Dispatchers.IO) {
            repeat(3) {
                try {
                    CsjRobot.getInstance().getState().closeOneFloorDoor()
                    delay(200)
                } catch (e: Exception) {
                    Log.e("RobotDoorRepositoryImpl", "closeOneFloorDoor: ${e.message}")
                }
            }
        }
    }

    override suspend fun closeTwoFloorDoor() {
        withContext(Dispatchers.IO) {
            repeat(3) {
                try {
                    CsjRobot.getInstance().getState().closeTwoFloorDoor()
                    delay(200)
                } catch (e: Exception) {
                    Log.e("RobotDoorRepositoryImpl", "closeTwoFloorDoor: ${e.message}")
                }
            }
        }
    }
}
