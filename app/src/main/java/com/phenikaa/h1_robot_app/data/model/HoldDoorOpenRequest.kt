package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName
import com.phenikaa.h1_robot_app.shared.constants.AppConstants

data class HoldDoorOpenRequest(
    @SerializedName("type") val type: String ?= AppConstants.HOLD_CAR_DOOR_TYPE,
    @SerializedName("buildingId") val buildingId: String,
    @SerializedName("callType") val callType: String ?= AppConstants.HOLD_CAR_DOOR_CALL_TYPE,
    @SerializedName("groupId") val groupId: String,
    @SerializedName("payload") val payload: HoldDoorOpenRequestPayload
)