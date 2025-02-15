package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class ModelFloorApi(
    @SerializedName("data") val data: DataFloorsWrapper
)

data class DataFloorsWrapper(
    @SerializedName("floors") val floors: List<DataFloors>,
    @SerializedName("page") val page: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("per_page") val perPage: Int,
)

data class DataFloors(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String,
    @SerializedName("floor_index") val floorIndex: Int,
    @SerializedName("note") val note: String ?= null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("deleted_at") val deletedAt: String ?= null,
)