package com.phenikaa.h1_robot_app.di

import com.google.gson.Gson
import com.phenikaa.h1_robot_app.data.datasource.websocket.BaseWebSocketDataSource
import com.phenikaa.h1_robot_app.data.repository.WebSocketRepositoryImpl
import com.phenikaa.h1_robot_app.domain.repository.WebSocketRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {
    @Provides
    @Singleton
    fun provideBaseWebSocketDataSource(
        gson: Gson,
        client: OkHttpClient
    ): BaseWebSocketDataSource {
        return BaseWebSocketDataSource(gson, client)
    }

    @Provides
    @Singleton
    fun provideWebSocketRepository(
        baseWebSocketDataSource: BaseWebSocketDataSource
    ): WebSocketRepository {
        return WebSocketRepositoryImpl(baseWebSocketDataSource)
    }
}