package com.phenikaa.h1_robot_app.di

import com.phenikaa.h1_robot_app.data.api.PhenikaaMecApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://38d0-2a09-bac5-d45a-16dc-00-247-128.ngrok-free.app/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePhenikaaMecApiService(retrofit: Retrofit): PhenikaaMecApiService {
        return retrofit.create(PhenikaaMecApiService::class.java)
    }
}