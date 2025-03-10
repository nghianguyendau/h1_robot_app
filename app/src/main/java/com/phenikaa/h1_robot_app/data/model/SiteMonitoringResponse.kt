package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

// respone_1_02
data class SiteMonitoringResponse(
    @SerializedName("data") val data: SiteMonitoringResponseData,
    @SerializedName("subtopic") val subtopic: String,
    @SerializedName("requestId") val requestId: String,
    @SerializedName("callType") val callType: String,
    @SerializedName("buildingId") val buildingId: String,
    @SerializedName("groupId") val groupId: String
)