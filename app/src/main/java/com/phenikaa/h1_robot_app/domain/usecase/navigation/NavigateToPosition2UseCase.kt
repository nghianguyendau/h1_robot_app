package com.phenikaa.h1_robot_app.domain.usecase.navigation

import com.phenikaa.h1_robot_app.domain.repository.NavigationRepository
import javax.inject.Inject

class NavigateToPosition2UseCase @Inject constructor(
    private val navigationRepository: NavigationRepository
) {
    suspend operator fun invoke(position: String) =
        navigationRepository.navigateToPosition2(position)

//    suspend fun navigateToPosition(position: RosPosition) = navigationRepository.navigateToPosition(position)

    suspend fun cancelNavi() = navigationRepository.cancelNavigation()

    suspend fun setSpeed(speed: Float) = navigationRepository.setSpeed(speed)

    suspend fun getSpeed() = navigationRepository.getSpeed()

}