package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class ResponseStateData(
    @SerializedName("request_id") val requestId: Long,
    @SerializedName("success") val success: Boolean
)
