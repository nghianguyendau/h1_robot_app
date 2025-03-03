package com.phenikaa.h1_robot_app.domain.entity

data class RobotRoute (
    val event: String = defaultEvent,
    val status : String = defaultStatus,
    val robotRouteTask : RobotRouteTask = defaultRobotRouteTask
){
    companion object {
        const val defaultEvent= ""
        const val defaultStatus = ""
        val defaultRobotRouteTask = RobotRouteTask()
    }
}