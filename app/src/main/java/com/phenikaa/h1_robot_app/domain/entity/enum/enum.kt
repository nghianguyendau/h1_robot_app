package com.phenikaa.h1_robot_app.domain.entity.enum

import com.phenikaa.h1_robot_app.shared.constants.AppConstants


enum class PinPasswordStatus() {
    ENABLE,
    TRUE,
    FALSE,
    DISABLE;
}

enum class RobotRouteTaskActionStatus(val value: String) {

    UnKnow(""),
    MoveToTarget(AppConstants.MOVE_TO_TARGET),
    CallLift(AppConstants.CALL_LIFT),
    SelectFloor(AppConstants.SELECT_FLOOR),
    ExitLift(AppConstants.EXIT_LIFT),
    DeliveryNotification(AppConstants.DELIVERY_NOTIFICATION),
    GoHome(AppConstants.GO_HOME);

    companion object {
        fun fromValue(value: String): RobotRouteTaskActionStatus {
            return entries.firstOrNull { it.value == value } ?: RobotRouteTaskActionStatus.UnKnow
        }
    }
}
