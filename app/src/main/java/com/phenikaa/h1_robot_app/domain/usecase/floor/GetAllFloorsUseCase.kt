package com.phenikaa.h1_robot_app.domain.usecase.floor

import com.phenikaa.h1_robot_app.data.model.ModelFloorApi
import com.phenikaa.h1_robot_app.domain.repository.PhenikaaMecRepository
import javax.inject.Inject

class GetAllFloorsUseCase @Inject constructor(
    private val repository: PhenikaaMecRepository
) {
    suspend operator fun invoke(page: Int, perPage: Int): Result<ModelFloorApi> {
        return repository.getAllFloors(page, perPage)
    }
}