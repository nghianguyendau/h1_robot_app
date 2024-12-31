package com.phenikaa.h1_robot_app.di

import com.csjbot.coshandler.core.CsjRobot
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RobotModule {

    @Provides
    @Singleton
    fun provideRobotInstance(): CsjRobot {
        return CsjRobot.getInstance()
    }
}