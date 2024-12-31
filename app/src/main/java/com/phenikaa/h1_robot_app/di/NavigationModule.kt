package com.phenikaa.h1_robot_app.di

import com.csjbot.coshandler.core.CsjRobot
import com.phenikaa.h1_robot_app.data.datasource.robot.RobotNaviDataSource
import com.phenikaa.h1_robot_app.data.repository.NavigationRepositoryImpl
import com.phenikaa.h1_robot_app.domain.repository.NavigationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.google.gson.Gson
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideRobotNaviDataSource(
        robot: CsjRobot,
        gson: Gson
    ): RobotNaviDataSource {
        return RobotNaviDataSource(robot, gson)
    }

    @Provides
    @Singleton
    fun provideNavigationRepository(
        naviDataSource: RobotNaviDataSource
    ): NavigationRepository {
        return NavigationRepositoryImpl(naviDataSource)
    }
}