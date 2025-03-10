package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class SiteMonitoringRequestPayload(
    @SerializedName("sub") val sub: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("subtopics") val subtopics: List<String>,
)