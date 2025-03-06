package com.phenikaa.h1_robot_app.domain.usecase.navigation

import com.phenikaa.h1_robot_app.domain.repository.NavigationRepository
import javax.inject.Inject

class MoveDirectionUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository
) {
    suspend fun moveDirection(direction: Int) = navigationRepository.moveDirection(direction)
    suspend fun moveBySerial(direction: Int) = navigationRepository.moveBySerial(direction)

    suspend fun moveSerial(linear: Int, angular: Int) = navigationRepository.moveSerial(linear, angular)

//    suspend operator fun invoke(angle: Int): Boolean {
//        return navigationRepository.moveAngle(angle)
//    }

    suspend fun goAngle(angle: Int) = navigationRepository.goAngle(angle)
    suspend fun moveAngle(angle: Int) = navigationRepository.moveAngle(angle)

    suspend fun goHome() = navigationRepository.goHome()
}