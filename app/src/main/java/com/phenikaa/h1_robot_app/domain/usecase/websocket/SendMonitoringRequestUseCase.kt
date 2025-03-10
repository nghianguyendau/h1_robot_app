package com.phenikaa.h1_robot_app.domain.usecase.websocket

import com.phenikaa.h1_robot_app.data.model.SiteMonitoringRequest
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository

class SendMonitoringRequestUseCase(
    private val webSocketRepository: WebSocketRepository
) {
     operator fun invoke(request: SiteMonitoringRequest) {
        webSocketRepository.sendSiteMonitoringRequest(request)
    }
}