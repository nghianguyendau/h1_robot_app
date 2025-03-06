package com.phenikaa.h1_robot_app.domain.entity


data class RobotRouteTask (
    val taskId : Int = defaultTaskId,
    val data : List<RobotRouteTaskDetail> = defaultData,
){
    companion object {
        val defaultTaskId = 0
        val defaultData: List<RobotRouteTaskDetail> = emptyList()
    }
}