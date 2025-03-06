package com.phenikaa.h1_robot_app.di

import com.google.gson.Gson
import com.phenikaa.h1_robot_app.data.source.robot.RobotNaviDataSource
import com.phenikaa.h1_robot_app.data.source.websocket.AppWebSocketService
import com.phenikaa.h1_robot_app.data.source.websocket.BaseWebSocketDataSource
import com.phenikaa.h1_robot_app.data.source.websocket.base.WebSocketClient
import com.phenikaa.h1_robot_app.data.mapper.RobotRouteMapper
import com.phenikaa.h1_robot_app.data.mapper.RobotRoutePoseMapper
import com.phenikaa.h1_robot_app.data.mapper.RobotRouteStepMapper
import com.phenikaa.h1_robot_app.data.mapper.RobotRouteTaskDetailMapper
import com.phenikaa.h1_robot_app.data.mapper.RobotRouteTaskMapper
import com.phenikaa.h1_robot_app.data.repository.WebSocketRepositoryImpl
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {
    @Provides
    fun provideBaseWebSocketDataSource(): BaseWebSocketDataSource {
        return BaseWebSocketDataSource()
    }

    @Provides
    fun provideRobotRouteMapper(
        robotRouteTaskMapper: RobotRouteTaskMapper
    ): RobotRouteMapper {
        return RobotRouteMapper(robotRouteTaskMapper)
    }

    @Provides
    fun provideRobotRouteTaskMapper(
        robotRouteTaskDetailMapper: RobotRouteTaskDetailMapper
    ): RobotRouteTaskMapper {
        return RobotRouteTaskMapper(robotRouteTaskDetailMapper)
    }

    @Provides
    fun provideRobotRouteTaskDetailMapper(
        robotRouteStepMapper: RobotRouteStepMapper
    ): RobotRouteTaskDetailMapper {
        return RobotRouteTaskDetailMapper(robotRouteStepMapper)
    }

    @Provides
    fun provideRobotRouteStepMapper(
        robotRoutePoseMapper: RobotRoutePoseMapper
    ): RobotRouteStepMapper {
        return RobotRouteStepMapper(robotRoutePoseMapper)
    }

    @Provides
    fun provideRobotPoseMapper(
    ): RobotRoutePoseMapper {
        return RobotRoutePoseMapper()
    }

    @Provides
    fun provideWebSocketClient(
    ): WebSocketClient {
        return WebSocketClient()
    }

    @Provides
    fun provideAppWebSocketService(
        webSocketClient: WebSocketClient,
        gson: Gson
    ): AppWebSocketService {
        return AppWebSocketService(webSocketClient, gson)
    }


    @Provides
    @Singleton
    fun provideWebSocketRepository(
        baseWebSocketDataSource: BaseWebSocketDataSource,
        appWebSocketService: AppWebSocketService,
        robotRouteMapper: RobotRouteMapper,
        robotNaviDataSource: RobotNaviDataSource,
        robotRoutePoseMapper: RobotRoutePoseMapper,
    ): WebSocketRepository {
        return WebSocketRepositoryImpl(
            baseWebSocketDataSource,
            appWebSocketService,
            robotRouteMapper,
            robotNaviDataSource,
            robotRoutePoseMapper
        )
    }
}