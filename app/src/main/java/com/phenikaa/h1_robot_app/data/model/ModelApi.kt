package com.phenikaa.h1_robot_app.data.model

data class ElevatorRequest(
    val current_floor: Int,
    val destination_floor: Int
)

data class ElevatorResponse(
    val status: Int,
    val error: Boolean,
    val message: String,
    val data: ElevatorData?
)

data class ElevatorData(
    val taskId: Int,
    val liftId: String
)

data class ElevatorTaskStatus(
    val taskId: Int,
    val status: Int, // 0: Thang máy bận, 1: Thành công
    val message: String
)

