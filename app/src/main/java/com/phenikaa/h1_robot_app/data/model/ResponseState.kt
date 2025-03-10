package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

// respone_1_03_2 H1 Drawio
data class ResponseState(
    @SerializedName("data") val data: ResponseStateData,
    @SerializedName("callType") val callType: String,
    @SerializedName("buildingId") val buildingId: String,
    @SerializedName("groupId") val groupId: String
)

