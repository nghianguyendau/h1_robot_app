package com.phenikaa.h1_robot_app.data.mapper

import com.phenikaa.h1_robot_app.data.mapper.base.BaseDataMapper
import com.phenikaa.h1_robot_app.data.model.RobotRouteData
import com.phenikaa.h1_robot_app.domain.entity.RobotRoute

class RobotRouteMapper(
    private val robotRouteTaskMapper: RobotRouteTaskMapper
) : BaseDataMapper<RobotRouteData, RobotRoute> {
    override fun mapToEntity(data: RobotRouteData?): RobotRoute {
        return RobotRoute(
            event = data?.event ?: RobotRoute.defaultEvent,
            status = data?.status ?: RobotRoute.defaultStatus,
            robotRouteTask = robotRouteTaskMapper.mapToEntity(data?.robotRouteTaskData)
        )
    }
}