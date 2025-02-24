package com.phenikaa.h1_robot_app.data.repository

import com.phenikaa.h1_robot_app.data.datasource.preference.Toan
import com.phenikaa.h1_robot_app.domain.repository.AppRepository
import com.phenikaa.h1_robot_app.shared.constants.AppConstants

class AppRepositoryImpl (
    private val appPreference: Toan
) : AppRepository {


    override fun getPassWord(): String {
        return appPreference.getPassword()
    }

    override fun initPassWord() {
        return appPreference.setPassword(AppConstants.PASS_WORD)
    }
}