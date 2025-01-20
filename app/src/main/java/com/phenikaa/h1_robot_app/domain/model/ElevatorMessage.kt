package com.phenikaa.h1_robot_app.domain.model

data class ElevatorMessage(
    var event: String,
    var data: ElevatorSendMessage
)

data class ElevatorSendMessage (
    var msg_id: String,
    var task_id: Int
)

data class ElevatorResponseMessage(
    var event: String,
    var msg_id: String
)

