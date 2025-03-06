package com.phenikaa.h1_robot_app.domain.entity

import com.phenikaa.h1_robot_app.domain.entity.enum.RobotRouteTaskActionStatus


data class RobotRouteStep (
    val robotRouteTaskActionStatus: RobotRouteTaskActionStatus = defaultAction,
    val robotRoutePose: RobotRoutePose = defaultRobotRoutePose,
    val pointId: Int = defaultPointId,
    val confirmationCode: String =defaultConfirmationCode
){
    companion object {
        val defaultAction = RobotRouteTaskActionStatus.UnKnow
        val defaultRobotRoutePose = RobotRoutePose()
        const val defaultPointId = 0
        const val defaultConfirmationCode = ""
    }
}