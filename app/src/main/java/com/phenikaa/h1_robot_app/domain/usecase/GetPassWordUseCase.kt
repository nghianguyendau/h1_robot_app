package com.phenikaa.h1_robot_app.domain.usecase

import com.phenikaa.h1_robot_app.domain.repository.AppRepository

class GetPassWordUseCase(
    private val appRepository: AppRepository
) {
    operator fun invoke(): String = appRepository.getPassWord()
}