package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class RobotRouteTaskData (
    @SerializedName("task_id") val taskId : Int,
    @SerializedName("data") val data : List<RobotRouteTaskDetailData>
)