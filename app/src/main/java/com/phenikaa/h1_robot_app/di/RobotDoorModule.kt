package com.phenikaa.h1_robot_app.di

import com.phenikaa.h1_robot_app.data.repository.RobotDoorRepositoryImpl
import com.phenikaa.h1_robot_app.domain.repository.RobotDoorRepository
import com.phenikaa.h1_robot_app.domain.usecase.robotdoor.RobotDoorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RobotDoorModule {
    @Provides
    @Singleton
    fun provideRobotDoorRepository(): RobotDoorRepository {
        return RobotDoorRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideRobotDoorUseCase(robotRepository: RobotDoorRepository): RobotDoorUseCase {
        return RobotDoorUseCase(robotRepository)
    }
}