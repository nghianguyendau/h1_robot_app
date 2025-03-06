package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class RobotRoutePoseData (
    @SerializedName("x") val x: Double,
    @SerializedName("y") val y: Double,
    @SerializedName("z") val z: Double,
    @SerializedName("rotation") val rotation: Double
)