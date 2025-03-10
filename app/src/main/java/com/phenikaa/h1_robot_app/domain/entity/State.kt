package com.phenikaa.h1_robot_app.domain.entity

data class State(
    val data: StateData,
    val callType: String,
    val buildingId: String,
    val groupId: String
)

