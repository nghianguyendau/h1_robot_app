package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName
import com.phenikaa.h1_robot_app.shared.constants.AppConstants

// msg_1_03. H1 Drawio
data class MakeElevatorCallRequest(
    @SerializedName("type") val type: String ?= AppConstants.MAKE_ELEVATOR_TYPE,
    @SerializedName("buildingId") val buildingId: String,
    @SerializedName("callType") val callType: String ?= AppConstants.MAKE_ELEVATOR_CALL_TYPE,
    @SerializedName("groupId") val groupId: String,
    @SerializedName("payload") val payload: MakeElevatorCallRequestPayload
)