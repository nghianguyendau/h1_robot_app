package com.phenikaa.h1_robot_app.domain.entity


data class RobotRouteStep (
    val action: String = defaultAction,
    val robotRoutePose: RobotRoutePose = defaultRobotRoutePose,
    val pointId: Int = defaultPointId,
    val confirmationCode: String =defaultConfirmationCode
){
    companion object {
        const val defaultAction = ""
        val defaultRobotRoutePose = RobotRoutePose()
        const val defaultPointId = 0
        const val defaultConfirmationCode = ""
    }
}