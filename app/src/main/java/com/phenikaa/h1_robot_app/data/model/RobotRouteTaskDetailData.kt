package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class RobotRouteTaskDetailData (
    @SerializedName("start_id") val startId: Int,
    @SerializedName("end_id") val endId: Int,
    @SerializedName("route_points") val routePoints: List<Int>,
    @SerializedName("navigation_steps") val navigationSteps: List<RobotRouteStepData>,
    @SerializedName("stage_id") val stageId: Int
)