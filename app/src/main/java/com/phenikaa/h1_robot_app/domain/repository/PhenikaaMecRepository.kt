package com.phenikaa.h1_robot_app.domain.repository

import com.phenikaa.h1_robot_app.data.model.ModelFloorApi
import com.phenikaa.h1_robot_app.data.model.ModelPointApi
import com.phenikaa.h1_robot_app.data.model.ModelPointsByFloorId
import com.phenikaa.h1_robot_app.data.model.NewPoint

interface PhenikaaMecRepository {
    suspend fun getPoints(page: Int, perPage: Int): Result<ModelPointApi>
    suspend fun getPointsByFloorId(floorId: Int): Result<ModelPointsByFloorId>
    suspend fun savePoint(point: NewPoint): Result<Unit>
    suspend fun updatePoint(id: Int, point: NewPoint): Result<Unit>
    suspend fun deletePoint(id: Int): Result<Unit>
    suspend fun getAllFloors(page: Int, perPage: Int): Result<ModelFloorApi>
}