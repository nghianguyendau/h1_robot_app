package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

// respone_1_03_1 H1 Drawio
data class ResponseConnection(
    @SerializedName("connectionId") val connectionId: String,
    @SerializedName("requestId") val requestId: Long,
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("data") val data: ResponseConnectionData
)
