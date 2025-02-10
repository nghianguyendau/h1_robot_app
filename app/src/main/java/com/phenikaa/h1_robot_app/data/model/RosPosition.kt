//package com.phenikaa.h1_robot_app.data.model
//
//import com.phenikaa.h1_robot_app.domain.model.Position
//import com.google.gson.annotations.SerializedName
//
//data class RosPosition(
//    @SerializedName("x") val x: Float,
//    @SerializedName("y") val y: Float,
//    @SerializedName("z") val z: Float,
//    @SerializedName("rotation") val rotation: Float
//) {
//    fun toDomainModel() = Position(x, y, z, rotation)
//
//    companion object {
//        fun fromDomainModel(position: Position) = RosPosition(
//            position.x,
//            position.y,
//            position.z,
//            position.rotation
//        )
//    }
//}

package com.phenikaa.h1_robot_app.data.model

import com.google.gson.annotations.SerializedName
import com.phenikaa.h1_robot_app.domain.model.Position

data class RosPosition(
    @SerializedName("pose_name") var poseName: String? = null, // Tên vị trí
    @SerializedName("pos") var pos: PosBean // Thông tin vị trí (x, y, z, rotation)
) {
    // Chuyển đổi RosPosition sang mô hình Domain
    fun toDomainModel() = Position(
        x = pos.x,
        y = pos.y,
        z = pos.z,
        rotation = pos.rotation,
//        poseName = poseName
    )

    companion object {
        // Chuyển đổi từ mô hình Domain sang RosPosition
        fun fromDomainModel(position: Position) = RosPosition(
            poseName = position.poseName,
            pos = PosBean(
                x = position.x,
                y = position.y,
                z = position.z,
                rotation = position.rotation
            )
        )
    }

    // Lớp con PosBean chứa thông tin tọa độ và góc quay
    data class PosBean(
        @SerializedName("x") var x: Float,
        @SerializedName("y") var y: Float,
        @SerializedName("z") var z: Float,
        @SerializedName("rotation") var rotation: Float
    ) {
        override fun toString(): String {
            return "PosBean(x=$x, y=$y, z=$z, rotation=$rotation)"
        }
    }

    override fun toString(): String {
        return "RosPosition(poseName='$poseName', pos=$pos)"
    }
}
