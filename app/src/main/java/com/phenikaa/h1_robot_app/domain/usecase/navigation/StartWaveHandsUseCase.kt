package com.phenikaa.h1_robot_app.domain.usecase.navigation

import com.phenikaa.h1_robot_app.domain.repository.NavigationRepository
import javax.inject.Inject

class StartWaveHandsUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository
) {
//    suspend operator fun invoke(time: Int) = navigationRepository.startWaveHands(time)
}