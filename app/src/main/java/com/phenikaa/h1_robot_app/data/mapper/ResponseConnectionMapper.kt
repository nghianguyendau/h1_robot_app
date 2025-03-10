package com.phenikaa.h1_robot_app.data.mapper

import com.phenikaa.h1_robot_app.data.mapper.base.BaseDataMapper
import com.phenikaa.h1_robot_app.data.model.ResponseConnection
import com.phenikaa.h1_robot_app.data.model.ResponseConnectionData
import com.phenikaa.h1_robot_app.domain.entity.Connection
import com.phenikaa.h1_robot_app.domain.entity.ConnectionData

class ResponseConnectionMapper: BaseDataMapper<ResponseConnection, Connection> {
    override fun mapToEntity(data: ResponseConnection?): Connection {
        return Connection(
            connectionId = data?.connectionId ?: "",
            requestId = data?.requestId ?: -1,
            statusCode = data?.statusCode ?: -1,
            data = data?.data?.toEntity() ?: ConnectionData(
                time = data?.data?.time ?: ""
            )
        )
    }

    private fun ResponseConnectionData.toEntity(): ConnectionData {
        return ConnectionData(
            time = time,
        )
    }
}

