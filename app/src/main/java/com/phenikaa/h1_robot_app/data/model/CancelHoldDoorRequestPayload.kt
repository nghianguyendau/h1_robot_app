package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class CancelHoldDoorRequestPayload(
    @SerializedName("session_id") val sessionId: String,
)