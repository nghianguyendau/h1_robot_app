package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName
import com.phenikaa.h1_robot_app.shared.constants.AppConstants

// msg_1_02. H1 Drawio
data class SiteMonitoringRequest (
    @SerializedName("type") val type: String ?= AppConstants.SITE_MONITORING_TYPE,
    @SerializedName("requestId") val requestId: String,
    @SerializedName("buildingId") val buildingId: String,
    @SerializedName("callType") val callType: String ?= AppConstants.SITE_MONITORING_CALL_TYPE,
    @SerializedName("groupId") val groupId: String,
    @SerializedName("payload") val payload: SiteMonitoringRequestPayload,
)