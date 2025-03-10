package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class SiteMonitoringResponseData(
    @SerializedName("time") val time: String ?= "",
    @SerializedName("dir") val dir: String ?= "",
    @SerializedName("coll") val coll: String ?= "",
    @SerializedName("moving_state") val movingState: String ?= "",
    @SerializedName("area") val area: Long ?= 0,
    @SerializedName("cur") val cur: Int ?= -1,
    @SerializedName("adv") val adv: Int ?= 0,
    @SerializedName("door") val door: Boolean ?= false,
    @SerializedName("lift_side") val liftSide: Int ?= -1,
    @SerializedName("state") val state: String ?= "",
    @SerializedName("landing") val landing: Int ?= -1,
    @SerializedName("lift_mode") val liftMode: Int ?= -1,
    @SerializedName("nominal_speed") val nominalSpeed: Int  ?= -1,
    @SerializedName("decks") val decks: List<DeckData> ?= emptyList(),
)

// int -1, string ="", long 0, boolean false, list empty