package com.phenikaa.h1_robot_app.data.source.preference

import android.content.Context
import android.content.SharedPreferences
import com.phenikaa.h1_robot_app.shared.constants.AppConstants
import com.phenikaa.h1_robot_app.shared.constants.AppPreferencesConstants

class AppPreferences (context: Context) {
    private var appSettings: SharedPreferences = context.getSharedPreferences(
        AppPreferencesConstants.APP_SETTING, Context.MODE_PRIVATE)

    private var defaultPassword = AppConstants.PASS_WORD

    fun getPassword(): String =
        appSettings.getString(AppPreferencesConstants.PASS_WORD, defaultPassword) ?: defaultPassword

    fun setPassword(password: String) {
        appSettings.edit().putString(AppPreferencesConstants.PASS_WORD, password).apply()
    }
}