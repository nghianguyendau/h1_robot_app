package com.phenikaa.h1_robot_app.data.mapper

import com.phenikaa.h1_robot_app.data.mapper.base.BaseDataMapper
import com.phenikaa.h1_robot_app.data.model.ResponseState
import com.phenikaa.h1_robot_app.data.model.ResponseStateData
import com.phenikaa.h1_robot_app.domain.entity.State
import com.phenikaa.h1_robot_app.domain.entity.StateData

class ResponseStateMapper: BaseDataMapper<ResponseState, State> {
    override fun mapToEntity(data: ResponseState?): State {
       return State(
           data = data?.data?.toEntity() ?: StateData(
               requestId = -1,
               success = false
           ),
           callType = data?.callType ?: "",
           buildingId = data?.buildingId ?: "",
           groupId = data?.groupId ?: "",
       )
    }

    private fun ResponseStateData.toEntity(): StateData {
        return StateData(
            requestId = requestId,
            success = success
        )
    }
}
