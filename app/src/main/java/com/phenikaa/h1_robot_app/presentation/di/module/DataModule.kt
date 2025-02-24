package com.phenikaa.h1_robot_app.presentation.di.module

import android.app.Application
import com.phenikaa.h1_robot_app.data.datasource.preference.Toan
import android.content.Context
import com.phenikaa.h1_robot_app.data.repository.AppRepositoryImpl
import com.phenikaa.h1_robot_app.domain.repository.AppRepository
import com.phenikaa.h1_robot_app.domain.usecase.GetPassWordUseCase
import com.phenikaa.h1_robot_app.domain.usecase.InitPassWordAppUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideAppRepository(
        appPreferences: Toan
    ): AppRepository {
        return AppRepositoryImpl(appPreferences)
    }

    @Provides
    fun provideAppPreferences(context: Context): Toan {
        return Toan(context)
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
}