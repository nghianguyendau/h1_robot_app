package com.phenikaa.h1_robot_app.domain.usecase.point

import com.phenikaa.h1_robot_app.data.model.NewPoint
import com.phenikaa.h1_robot_app.domain.repository.PhenikaaMecRepository
import javax.inject.Inject

class SavePointUseCase @Inject constructor(
    private val repository: PhenikaaMecRepository
) {
    suspend operator fun invoke(point: NewPoint): Result<Unit> {
        return repository.savePoint(point)
    }
}