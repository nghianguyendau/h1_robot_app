package com.phenikaa.h1_robot_app.domain.entity


data class RobotRouteTaskDetail (
    val startId: Int = defaultStartId,
    val endId: Int = defaultEndId,
    val routePoints: List<Int> = defaultRoutePoints,
    val navigationSteps: List<RobotRouteStep> = defaultNavigationSteps,
    val stageId: Int = defaultStageId
){
    companion object {
        const val defaultStartId = 0
        const val defaultEndId = 0
        val defaultRoutePoints: List<Int> = emptyList()
        val defaultNavigationSteps: List<RobotRouteStep> = emptyList()
        const val defaultStageId = 0
    }
}