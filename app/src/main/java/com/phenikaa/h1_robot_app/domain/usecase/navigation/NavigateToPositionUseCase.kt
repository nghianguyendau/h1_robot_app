package com.phenikaa.h1_robot_app.domain.usecase.navigation

import com.phenikaa.h1_robot_app.domain.model.Position
import com.phenikaa.h1_robot_app.domain.repository.NavigationRepository
import javax.inject.Inject

class NavigateToPositionUseCase @Inject constructor(
    private val navigationRepository: NavigationRepository
) {
    suspend operator fun invoke(position: Position) =
        navigationRepository.navigateToPosition(position)
}