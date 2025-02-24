package com.phenikaa.h1_robot_app.domain.repository

import com.phenikaa.h1_robot_app.shared.constants.AppConstants
import javax.inject.Inject

interface AppRepository {
    fun getPassWord(): String

    fun initPassWord()
}