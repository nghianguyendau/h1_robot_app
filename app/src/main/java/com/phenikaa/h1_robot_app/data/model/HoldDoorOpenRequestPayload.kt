package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class HoldDoorOpenRequestPayload(
    @SerializedName("request_id") val requestId: Long,
    @SerializedName("time") val time: String,
    @SerializedName("lift_deck") val liftDeck: Long,
    @SerializedName("served_area") val servedArea: Long,
    @SerializedName("hard_time") val hardTime: Int,
    @SerializedName("soft_time") val softTime: Int
)