package com.phenikaa.h1_robot_app.presentation.di.module

import android.app.Application
import com.phenikaa.h1_robot_app.data.datasource.preference.AppPreferences
import android.content.Context
import com.phenikaa.h1_robot_app.data.repository.AppRepositoryImpl
import com.phenikaa.h1_robot_app.domain.repository.AppRepository
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import com.phenikaa.h1_robot_app.domain.usecase.GetPassWordUseCase
import com.phenikaa.h1_robot_app.domain.usecase.InitPassWordAppUseCase
import com.phenikaa.h1_robot_app.domain.usecase.robot_route.RobotRouteConnectUseCase
import com.phenikaa.h1_robot_app.domain.usecase.robot_route.RobotRouteReceiveMessagesUseCase
import com.phenikaa.h1_robot_app.domain.usecase.robot_route.RobotRouteSendMessengerUseCase
import com.phenikaa.h1_robot_app.domain.usecase.robot_route.robotRouteNaviPosUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideAppRepository(
        appPreferences: AppPreferences
    ): AppRepository {
        return AppRepositoryImpl(appPreferences)
    }

    @Provides
    fun provideAppPreferences(context: Context): AppPreferences {
        return AppPreferences(context)
    }

    @Provides
    fun provideContext(application: Application): Context = application.applicationContext


    @Provides
    fun provideGetPassWordUseCase(appRepository: AppRepository): GetPassWordUseCase {
        return GetPassWordUseCase(appRepository)
    }

    @Provides
    fun provideInitPassWordAppUseCase(appRepository: AppRepository): InitPassWordAppUseCase {
        return InitPassWordAppUseCase(appRepository)
    }

    @Provides
    fun provideRobotRouteConnectUseCase(webSocketRepository: WebSocketRepository): RobotRouteConnectUseCase {
        return RobotRouteConnectUseCase(webSocketRepository)
    }

    @Provides
    fun provideRobotRouteReceiveMessagesUseCase(webSocketRepository: WebSocketRepository): RobotRouteReceiveMessagesUseCase {
        return RobotRouteReceiveMessagesUseCase(webSocketRepository)
    }

    @Provides
    fun provideRobotRouteSendMessengerUseCase(webSocketRepository: WebSocketRepository): RobotRouteSendMessengerUseCase {
        return RobotRouteSendMessengerUseCase(webSocketRepository)
    }

    @Provides
    fun provideRobotRouteNaviPosUseCase(webSocketRepository: WebSocketRepository): robotRouteNaviPosUseCase {
        return robotRouteNaviPosUseCase(webSocketRepository)
    }

}