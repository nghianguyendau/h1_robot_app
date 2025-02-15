package com.phenikaa.h1_robot_app.domain.usecase.point

import com.phenikaa.h1_robot_app.data.model.ModelPointsByFloorId
import com.phenikaa.h1_robot_app.domain.repository.PhenikaaMecRepository
import javax.inject.Inject

class GetPointsByFloorIdUseCase @Inject constructor(
    private val repository: PhenikaaMecRepository
) {
    suspend operator fun invoke(floorId: Int): Result<ModelPointsByFloorId> {
        return repository.getPointsByFloorId(floorId)
    }
}