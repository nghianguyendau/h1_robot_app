package com.phenikaa.h1_robot_app

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.csjbot.coshandler.core.CsjRobot
import com.csjbot.coshandler.listener.OnAuthenticationListener
import com.phenikaa.h1_robot_app.utils.SharedPreferencesSDCard
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initializeRobotSDK()
        initializeSharedPreferences()

    }

    private fun initializeRobotSDK() {
        CsjRobot.authentication(this, "1b0bb9fa-b9a2-4e37-88e6-410ab33670c0",
            "D89E442C9C1082919A38B7F5CB9A45D8",
            object : OnAuthenticationListener {
                override fun success() {
                    Log.d("TAG", "Authorization succeeded!")
                    CsjRobot.getInstance().init(applicationContext)
                }

                override fun error() {
                    Log.d("TAG", "Privilege grant failed!")
                }
            })
    }

    private fun initializeSharedPreferences() {
        SharedPreferencesSDCard.init(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
