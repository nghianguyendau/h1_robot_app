package com.phenikaa.h1_robot_app.data.model

import com.phenikaa.h1_robot_app.domain.model.Position
import com.google.gson.annotations.SerializedName

data class RosPosition(
    @SerializedName("x") val x: Float,
    @SerializedName("y") val y: Float,
    @SerializedName("z") val z: Float,
    @SerializedName("rotation") val rotation: Float
) {
    fun toDomainModel() = Position(x, y, z, rotation)

    companion object {
        fun fromDomainModel(position: Position) = RosPosition(
            position.x,
            position.y,
            position.z,
            position.rotation
        )
    }
}