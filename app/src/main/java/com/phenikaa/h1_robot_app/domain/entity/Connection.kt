package com.phenikaa.h1_robot_app.domain.entity

data class Connection(
    val connectionId: String,
    val requestId: Long,
    val statusCode: Int,
    val data: ConnectionData
)
