package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class RobotRouteData (
    @SerializedName("event") val event : String,
    @SerializedName("status") val status : String,
    @SerializedName("data") val robotRouteTaskData : RobotRouteTaskData
)
