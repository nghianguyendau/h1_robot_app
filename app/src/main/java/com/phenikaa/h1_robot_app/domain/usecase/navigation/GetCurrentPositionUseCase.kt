package com.phenikaa.h1_robot_app.domain.usecase.navigation

import com.phenikaa.h1_robot_app.domain.repository.NavigationRepository
import javax.inject.Inject

class GetCurrentPositionUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository
) {
    suspend operator fun invoke() = navigationRepository.getCurrentPosition()
}