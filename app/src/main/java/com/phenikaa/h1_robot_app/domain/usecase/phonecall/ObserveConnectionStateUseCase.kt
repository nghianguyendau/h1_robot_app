package com.phenikaa.h1_robot_app.domain.usecase.phonecall

import com.phenikaa.h1_robot_app.data.datasource.websocket.ConnectionState
import com.phenikaa.h1_robot_app.data.repository.PhoneCallRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveConnectionStateUseCase @Inject constructor(
    private val phoneCallRepository: PhoneCallRepository
) {
    operator fun invoke(): Flow<ConnectionState> {
        return phoneCallRepository.observeConnectionState()
    }
}