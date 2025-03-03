package com.phenikaa.h1_robot_app.data.mapper

import com.phenikaa.h1_robot_app.data.mapper.base.BaseDataMapper
import com.phenikaa.h1_robot_app.data.model.RobotRouteStepData
import com.phenikaa.h1_robot_app.domain.entity.RobotRouteStep

class RobotRouteStepMapper(
    private val robotRoutePoseMapper: RobotRoutePoseMapper
) : BaseDataMapper<RobotRouteStepData, RobotRouteStep> {

    override fun mapToEntity(data: RobotRouteStepData?): RobotRouteStep {
        return RobotRouteStep(
            action = data?.action ?: RobotRouteStep.defaultAction,
            robotRoutePose = robotRoutePoseMapper.mapToEntity(data?.robotRoutePose),
            pointId = data?.pointId ?: RobotRouteStep.defaultPointId,
            confirmationCode = data?.confirmationCode ?: RobotRouteStep.defaultConfirmationCode
        )
    }
}