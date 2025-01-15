package com.phenikaa.h1_robot_app.domain.model

data class PhoneCallMessage (
    var event: String,
    var number: String,
)

data class PhoneCallResponseMessage (
    var event: String,
    var status: Int,
    var info: String
)