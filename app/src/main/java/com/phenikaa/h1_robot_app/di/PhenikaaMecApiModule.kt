package com.phenikaa.h1_robot_app.di

import com.phenikaa.h1_robot_app.data.api.PhenikaaMecApiService
import com.phenikaa.h1_robot_app.data.repository.PhenikaaMecRepositoryImpl
import com.phenikaa.h1_robot_app.domain.repository.PhenikaaMecRepository
import com.phenikaa.h1_robot_app.domain.usecase.floor.GetAllFloorsUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.DeletePointUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.GetPointsByFloorIdUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.GetPointsUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.SavePointUseCase
import com.phenikaa.h1_robot_app.domain.usecase.point.UpdatePointUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhenikaaMecApiModule {

    @Provides
    @Singleton
    fun providePhenikaaMecRepository(apiService: PhenikaaMecApiService): PhenikaaMecRepository {
        return PhenikaaMecRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetPointsUseCase(repository: PhenikaaMecRepository): GetPointsUseCase {
        return GetPointsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPointsByFloorIdUseCase(repository: PhenikaaMecRepository): GetPointsByFloorIdUseCase {
        return GetPointsByFloorIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSavePointUseCase(repository: PhenikaaMecRepository): SavePointUseCase {
        return SavePointUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdatePointUseCase(repository: PhenikaaMecRepository): UpdatePointUseCase {
        return UpdatePointUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeletePointUseCase(repository: PhenikaaMecRepository): DeletePointUseCase {
        return DeletePointUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllFloorsUseCase(repository: PhenikaaMecRepository): GetAllFloorsUseCase {
        return GetAllFloorsUseCase(repository)
    }
}
