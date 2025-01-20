package com.phenikaa.h1_robot_app.domain.usecase.elevator

import com.phenikaa.h1_robot_app.data.repository.ElevatorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MonitorElevatorTaskUseCase @Inject constructor(
    private val repository: ElevatorRepository
) {
    suspend operator fun invoke(taskId: Int): Flow<String> {
        return repository.receiveMessages()
    }
}
