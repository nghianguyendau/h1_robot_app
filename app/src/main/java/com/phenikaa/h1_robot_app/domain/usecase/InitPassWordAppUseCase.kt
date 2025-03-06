package com.phenikaa.h1_robot_app.domain.usecase

import com.phenikaa.h1_robot_app.domain.repository.AppRepository

class InitPassWordAppUseCase(
    private val appRepository: AppRepository
) {
    operator fun invoke() = appRepository.initPassWord()
}