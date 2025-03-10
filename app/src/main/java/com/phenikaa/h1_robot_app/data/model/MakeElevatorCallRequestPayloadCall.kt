package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class MakeElevatorCallRequestPayloadCall(
    @SerializedName("action") val action: Long,
    @SerializedName("destination") val destination: Long
)