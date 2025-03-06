package com.phenikaa.h1_robot_app.domain.entity


data class RobotRoutePose (
    val x: Double = defaultX,
    val y: Double = defaultY,
    val z: Double = defaultZ,
    val rotation: Double = defaultRotation
){
    companion object {
        val defaultX = 0.0
        val defaultY = 0.0
        val defaultZ = 0.0
        val defaultRotation = 0.0
    }
}