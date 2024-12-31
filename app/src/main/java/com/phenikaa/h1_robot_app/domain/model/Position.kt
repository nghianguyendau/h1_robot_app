package com.phenikaa.h1_robot_app.domain.model

data class Position(
    val x: Float,
    val y: Float,
    val z: Float = 0f,
    val rotation: Float
)