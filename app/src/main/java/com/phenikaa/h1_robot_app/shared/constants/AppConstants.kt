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
    }
}