package com.phenikaa.h1_robot_app.domain.usecase.point

import com.phenikaa.h1_robot_app.data.model.ModelPointApi
import com.phenikaa.h1_robot_app.domain.repository.PhenikaaMecRepository
import javax.inject.Inject

class GetPointsUseCase @Inject constructor(
    private val repository: PhenikaaMecRepository
) {
    suspend operator fun invoke(page: Int, perPage: Int): Result<ModelPointApi> {
        return repository.getPoints(page, perPage)
    }
}