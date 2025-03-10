package com.phenikaa.h1_robot_app.shared.constants

class AppConstants private constructor() {
    companion object {
        const val PASS_WORD = "123459"

        // Robot route action
        const val MOVE_TO_TARGET = "MoveToTarget"
        const val CALL_LIFT = "CallLift"
        const val SELECT_FLOOR = "SelectFloor"
        const val EXIT_LIFT = "ExitLift"
        const val DELIVERY_NOTIFICATION = "DeliveryNotification"
        const val GO_HOME = "GoHome"
        const val TASK_STEP_CONFIRMED = "task_step_confirmed"
        const val CANCEL_TASK = "cancel_task"
        const val STAGE_FINISH = "stage_finished"

        // KONE API Site monitoring
        const val SITE_MONITORING_TYPE = "site_monitoring"
        const val SITE_MONITORING_CALL_TYPE = "monitor"
        const val SITE_MONITORING_SUBTOPIC_POSITION = "lift_1/position"
        const val SITE_MONITORING_SUBTOPIC_DOORS = "lift_1/doors"
        const val SITE_MONITORING_SUBTOPIC_STATUS = "lift_1/status"

        // KONE API Make Elevator Call
        const val MAKE_ELEVATOR_TYPE = "lift-call-api-v2"
        const val MAKE_ELEVATOR_CALL_TYPE = "action"

        // KONE API Hold Car Door
        const val HOLD_CAR_DOOR_TYPE = "lift-call-api-v2"
        const val HOLD_CAR_DOOR_CALL_TYPE = "hold_open"

        // KONE API Cancel Hold Car Door
        const val CANCEL_HOLD_CAR_DOOR_TYPE = "lift-call-api-v2"
        const val CANCEL_HOLD_CAR_DOOR_CALL_TYPE = "delete"
    }
}