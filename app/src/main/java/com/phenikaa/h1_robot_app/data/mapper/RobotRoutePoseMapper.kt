package com.phenikaa.h1_robot_app.data.mapper

import com.phenikaa.h1_robot_app.data.mapper.base.BaseDataMapper
import com.phenikaa.h1_robot_app.data.mapper.base.DataMapper
import com.phenikaa.h1_robot_app.data.model.RobotRoutePoseData
import com.phenikaa.h1_robot_app.data.model.RobotRouteTaskDetailData
import com.phenikaa.h1_robot_app.domain.entity.RobotRoutePose
import com.phenikaa.h1_robot_app.domain.entity.RobotRouteTaskDetail


class RobotRoutePoseMapper : DataMapper<RobotRoutePoseData, RobotRoutePose> {
    override fun mapToEntity(data: RobotRoutePoseData?): RobotRoutePose {
        return RobotRoutePose(
            x = data?.x ?: RobotRoutePose.defaultX,
            y = data?.y ?: RobotRoutePose.defaultY,
            z = data?.z ?: RobotRoutePose.defaultZ,
            rotation = data?.rotation ?: RobotRoutePose.defaultRotation
        )
    }

    override fun mapToData(entity: RobotRoutePose): RobotRoutePoseData {
        return RobotRoutePoseData(
            x = entity.x,
            y = entity.y,
            z = entity.z,
            rotation = entity.rotation
        )
    }
}