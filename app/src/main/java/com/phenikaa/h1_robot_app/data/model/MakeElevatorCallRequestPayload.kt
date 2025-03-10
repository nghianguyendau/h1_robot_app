package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class MakeElevatorCallRequestPayload(
    @SerializedName("request_id") val requestId: Long,
    @SerializedName("area") val area: Long,
    @SerializedName("time") val time: String,
    @SerializedName("terminal") val terminal: Int,
    @SerializedName("call") val call: MakeElevatorCallRequestPayloadCall
)