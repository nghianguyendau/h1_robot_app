package com.phenikaa.h1_robot_app.data.mapper

import com.phenikaa.h1_robot_app.data.mapper.base.BaseDataMapper
import com.phenikaa.h1_robot_app.data.model.RobotRouteTaskDetailData
import com.phenikaa.h1_robot_app.domain.entity.RobotRouteTaskDetail


class RobotRouteTaskDetailMapper(
    private val robotRouteStepMapper: RobotRouteStepMapper
) : BaseDataMapper<RobotRouteTaskDetailData, RobotRouteTaskDetail> {
    override fun mapToEntity(data: RobotRouteTaskDetailData?): RobotRouteTaskDetail {
        return RobotRouteTaskDetail(
            startId = data?.startId ?: RobotRouteTaskDetail.defaultStartId,
            endId = data?.endId ?: RobotRouteTaskDetail.defaultEndId,
            routePoints = data?.routePoints ?: RobotRouteTaskDetail.defaultRoutePoints,
            stageId = data?.stageId ?: RobotRouteTaskDetail.defaultStageId,
            navigationSteps = robotRouteStepMapper.mapToListEntity(data?.navigationSteps)
        )
    }
}