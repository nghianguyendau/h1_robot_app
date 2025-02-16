package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName

data class ModelPointApi(
    @SerializedName("data") val data: DataWrapper
)

data class DataWrapper(
    @SerializedName("points") val points: List<Point>,
    @SerializedName("page") val page: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("per_page") val perPage: Int,
)

data class Point(
    @SerializedName("id") val id: Int,
    @SerializedName("floor_code") val floorCode: String ?= null,
//    @SerializedName("code") val code: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("x") val x: Float,
    @SerializedName("y") val y: Float,
    @SerializedName("z") val z: Float?,
    @SerializedName("rotation") val rotation: Float,
//    @SerializedName("floor_id") val floorId: Int,
    @SerializedName("type") val type: Int,
    @SerializedName("floor") val floor: Floor?,

){
    fun getTypeLabel(): String {
        return when (type) {
            0 -> "Điểm stop"
            1 -> "Điểm trong cabin"
            2 -> "Điểm vào cabin"
            3 -> "Điểm rời cabin"
            4 -> "Điểm bắt đầu"
            else -> "Unknown"
        }
    }
}


data class NewPoint(
    @SerializedName("floor_code") val floorCode: String ?= null,
    @SerializedName("name") val name: String?,
    @SerializedName("x") val x: Float,
    @SerializedName("y") val y: Float,
    @SerializedName("z") val z: Float?,
    @SerializedName("rotation") val rotation: Float,
    @SerializedName("type") val type: Int,
)
data class Floor(
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String,
)

data class ModelPointsByFloorId(
    @SerializedName("data") val data: List<DataPointsByFloorIdWrapper>
)

data class DataPointsByFloorIdWrapper(
    @SerializedName("id") val id: Int,
    @SerializedName("floor_id") val floor_id: Int,
    @SerializedName("lift_id") val lift_id: Int,
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("notes") val notes: String ?= null,
    @SerializedName("rotation") val rotation: Float,
    @SerializedName("type") val type: Int,
    @SerializedName("x") val x: Float,
    @SerializedName("y") val y: Float,
    @SerializedName("z") val z: Float,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("deleted_at") val deletedAt: String ?= null,
)





