package com.phenikaa.h1_robot_app.domain.usecase.elevator

import com.phenikaa.h1_robot_app.data.repository.ElevatorRepository
import javax.inject.Inject

class CallElevatorUseCase @Inject constructor(
    private val repository: ElevatorRepository
) {
    suspend operator fun invoke(currentFloor: Int, destinationFloor: Int): Result<Unit> {
        return try {
            val message = """{
                "currentFloor": $currentFloor,
                "destinationFloor": $destinationFloor
            }"""
            repository.sendMessage(message)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
