package com.phenikaa.h1_robot_app.di

import com.google.gson.Gson
import com.phenikaa.h1_robot_app.data.source.websocket.BaseWebSocketDataSource
import com.phenikaa.h1_robot_app.data.repository.ElevatorRepository
import com.phenikaa.h1_robot_app.domain.usecase.elevator.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ElevatorModule {

    // Repository
    @Provides
    @Singleton
    fun provideElevatorRepository(
        baseWebSocketDataSource: BaseWebSocketDataSource,
        gson: Gson
    ): ElevatorRepository {
        return ElevatorRepository(baseWebSocketDataSource, gson)
    }

    // UseCases
    @Provides
    fun provideConnectElevatorWebSocketUseCase(
        repository: ElevatorRepository
    ): ConnectElevatorWebSocketUseCase {
        return ConnectElevatorWebSocketUseCase(repository)
    }

    @Provides
    fun provideDisconnectElevatorWebSocketUseCase(
        repository: ElevatorRepository
    ): DisconnectElevatorWebSocketUseCase {
        return DisconnectElevatorWebSocketUseCase(repository)
    }

    @Provides
    fun provideCallElevatorUseCase(
        repository: ElevatorRepository
    ): CallElevatorUseCase {
        return CallElevatorUseCase(repository)
    }

    @Provides
    fun provideMonitorElevatorTaskUseCase(
        repository: ElevatorRepository
    ): MonitorElevatorTaskUseCase {
        return MonitorElevatorTaskUseCase(repository)
    }

//    @Provides
//    fun provideObserveElevatorMessagesUseCase(
//        repository: ElevatorRepository
//    ): ObserveElevatorMessagesUseCase {
//        return ObserveElevatorMessagesUseCase(repository)
//    }
//
//    @Provides
//    fun provideObserveElevatorConnectionStateUseCase(
//        repository: ElevatorRepository
//    ): ObserveElevatorConnectionStateUseCase {
//        return ObserveElevatorConnectionStateUseCase(repository)
//    }
}
