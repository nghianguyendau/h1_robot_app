package com.phenikaa.h1_robot_app.domain.usecase.point

import com.phenikaa.h1_robot_app.domain.repository.PhenikaaMecRepository
import javax.inject.Inject

class DeletePointUseCase @Inject constructor(
    private val repository: PhenikaaMecRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deletePoint(id)
    }
}