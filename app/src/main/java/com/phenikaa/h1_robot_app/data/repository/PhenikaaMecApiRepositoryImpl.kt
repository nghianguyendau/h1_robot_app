package com.phenikaa.h1_robot_app.data.repository

import android.util.Log
import com.phenikaa.h1_robot_app.data.api.PhenikaaMecApiService
import com.phenikaa.h1_robot_app.data.model.ModelFloorApi
import com.phenikaa.h1_robot_app.data.model.ModelPointApi
import com.phenikaa.h1_robot_app.data.model.ModelPointsByFloorId
import com.phenikaa.h1_robot_app.data.model.NewPoint
import com.phenikaa.h1_robot_app.domain.repository.PhenikaaMecRepository
import javax.inject.Inject

class PhenikaaMecRepositoryImpl @Inject constructor(
    private val apiService: PhenikaaMecApiService
) : PhenikaaMecRepository {

    override suspend fun getPoints(page: Int, perPage: Int): Result<ModelPointApi> {
        return try {
            val response = apiService.getPoints(page, perPage)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPointsByFloorId(floorId: Int): Result<ModelPointsByFloorId> {
        return try {
            val response = apiService.getPointsByFloorId(floorId)
            Log.d("API_SUCCESS", "Received data: ${response.data}")
            Result.success(response)
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error fetching points: ${e.message}", e)
            Result.failure(e)
        }
    }


    override suspend fun savePoint(point: NewPoint): Result<Unit> {
        return try {
            val response = apiService.savePoint(point)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to save point: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePoint(id: Int, point: NewPoint): Result<Unit> {
        return try {
            val response = apiService.updatePoint(id, point)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update point: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePoint(id: Int): Result<Unit> {
        return try {
            val response = apiService.deletePoint(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete point: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllFloors(page: Int, perPage: Int): Result<ModelFloorApi> {
        return try {
            val response = apiService.getAllFloors(page, perPage)
            Log.d("API_SUCCESS", "Received data: ${response.data}")
            Result.success(response)
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error fetching points: ${e.message}", e)
            Result.failure(e)
        }
    }
}