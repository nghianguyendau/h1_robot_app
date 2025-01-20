package com.phenikaa.h1_robot_app.state

enum class RobotState {
    IDLE,
    CALLING_ELEVATOR,
    WAITING_FOR_ELEVATOR,
    MOVING_TO_CABIN,
    INSIDE_CABIN,
    MOVING_TO_DESTINATION,
    TASK_COMPLETED,
}