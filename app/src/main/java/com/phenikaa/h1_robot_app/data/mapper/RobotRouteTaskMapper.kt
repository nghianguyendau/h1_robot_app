package com.phenikaa.h1_robot_app.data.mapper

import com.phenikaa.h1_robot_app.data.mapper.base.BaseDataMapper
import com.phenikaa.h1_robot_app.data.model.RobotRouteTaskData
import com.phenikaa.h1_robot_app.domain.entity.RobotRouteTask

class RobotRouteTaskMapper(
    private val robotRouteTaskDetailMapper: RobotRouteTaskDetailMapper
) : BaseDataMapper<RobotRouteTaskData, RobotRouteTask> {
    override fun mapToEntity(data: RobotRouteTaskData?): RobotRouteTask {
        return RobotRouteTask(
            taskId = data?.taskId ?: RobotRouteTask.defaultTaskId,
            data = robotRouteTaskDetailMapper.mapToListEntity(data?.data)
        )
    }
}