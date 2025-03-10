package com.phenikaa.h1_robot_app.domain.usecase.websocket

import com.phenikaa.h1_robot_app.domain.entity.SiteMonitoring
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow

class ReceiveSiteMonitoringResponseUseCase(
    private val webSocketRepository: WebSocketRepository
) {
     operator fun invoke(): Flow<SiteMonitoring> {
        return webSocketRepository.receiveSiteMonitoringResponse()
    }
}