package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class RobotRouteStepData (
    @SerializedName("action") val action: String,
    @SerializedName("pose") val robotRoutePose: RobotRoutePoseData,
    @SerializedName("point_id") val pointId: Int,
    @SerializedName("confirmation_code") val confirmationCode: String
)