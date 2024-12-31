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
        // Xác thực SDKauth
        CsjRobot.authentication(this, "", "", object : OnAuthenticationListener {
            override fun success() {
                Log.d("TAG", "Authorization succeeded!")
            }

            override fun error() {
                Log.d("TAG", "Privilege grant failed!")
            }
        })

        // Đợi xác thực hoàn tất
        try {
            Thread.sleep(1500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        // Cấu hình các module của SDK
        CsjRobot.enableAsr(true)  // Kích hoạt module nhận diện giọng nói
        CsjRobot.enableFace(true) // Kích hoạt module nhận diện khuôn mặt
        CsjRobot.enableSlam(true) // Kích hoạt module SLAM (điều hướng)
        CsjRobot.setIpAndrPort("127.0.0.1", 60002) // Địa chỉ giao tiếp
        CsjRobot.setRobotType(CsjRobot.RobotType.TIMO) // Cấu hình loại robot
        CsjRobot.getInstance().init(this) // Khởi tạo SDK
    }

    private fun initializeSharedPreferences() {
        SharedPreferencesSDCard.init(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}