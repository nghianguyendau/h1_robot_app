package com.phenikaa.h1_robot_app.domain.usecase.elevator

import com.phenikaa.h1_robot_app.data.repository.ElevatorRepository
import javax.inject.Inject

class ConnectElevatorWebSocketUseCase @Inject constructor(
    private val elevatorRepository: ElevatorRepository
) {
    operator fun invoke() {
        elevatorRepository.connect()
    }
}