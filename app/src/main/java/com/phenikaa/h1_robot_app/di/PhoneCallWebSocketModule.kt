package com.phenikaa.h1_robot_app.di

import com.google.gson.Gson
import com.phenikaa.h1_robot_app.data.datasource.websocket.BaseWebSocketDataSource
import com.phenikaa.h1_robot_app.data.repository.PhoneCallRepository
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.ConnectPhoneCallWebSocketUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.DisconnectPhoneCallWebSocketUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.MakePhoneCallUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.ObserveConnectionStateUseCase
import com.phenikaa.h1_robot_app.domain.usecase.phonecall.ObservePhoneCallMessagesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhoneCallModule {
    // Repository
    @Provides
    @Singleton
    fun providePhoneCallRepository(
        baseWebSocketDataSource: BaseWebSocketDataSource,
        gson: Gson
    ): PhoneCallRepository {
        return PhoneCallRepository(baseWebSocketDataSource, gson)
    }

    // UseCases
    @Provides
    fun provideConnectPhoneCallWebSocketUseCase(
        repository: PhoneCallRepository
    ): ConnectPhoneCallWebSocketUseCase {
        return ConnectPhoneCallWebSocketUseCase(repository)
    }

    @Provides
    fun provideDisconnectPhoneCallWebSocketUseCase(
        repository: PhoneCallRepository
    ): DisconnectPhoneCallWebSocketUseCase {
        return DisconnectPhoneCallWebSocketUseCase(repository)
    }

    @Provides
    fun provideMakePhoneCallUseCase(
        repository: PhoneCallRepository
    ): MakePhoneCallUseCase {
        return MakePhoneCallUseCase(repository)
    }

    @Provides
    fun provideObservePhoneCallMessagesUseCase(
        repository: PhoneCallRepository
    ): ObservePhoneCallMessagesUseCase {
        return ObservePhoneCallMessagesUseCase(repository)
    }

    @Provides
    fun provideObserveConnectionStateUseCase(
        repository: PhoneCallRepository
    ): ObserveConnectionStateUseCase {
        return ObserveConnectionStateUseCase(repository)
    }
}