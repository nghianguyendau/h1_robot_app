package com.phenikaa.h1_robot_app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.csjbot.coshandler.core.CsjRobot
import com.csjbot.coshandler.listener.OnAuthenticationListener
import com.csjbot.coshandler.listener.OnDoubleDoorStateListener
import com.csjbot.coshandler.listener.OnMapListListener
import com.csjbot.coshandler.listener.OnMapStateListener
import com.csjbot.coshandler.listener.OnPositionListener
import com.csjbot.coshandler.listener.OnRobotStateListener
import com.csjbot.coshandler.listener.OnSpeedGetListener
import com.csjbot.coshandler.listener.OnWarningCheckSelfListener
import com.phenikaa.h1_robot_app.domain.usecase.robotdoor.RobotDoorUseCase
import com.phenikaa.h1_robot_app.utils.SharedPreferencesSDCard
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


@HiltAndroidApp
class MyApplication : MultiDexApplication() {

    @Inject
    lateinit var robotDoorUseCase: RobotDoorUseCase

    private val _batteryLevel = MutableStateFlow(100)
    val batteryLevel = _batteryLevel.asStateFlow()

    private val _chargeState = MutableStateFlow(false)
    val chargeState = _chargeState.asStateFlow()

    override fun onCreate() {
        super.onCreate()
        initializeRobotSDK()
        initializeSharedPreferences()
        GlobalScope.launch {
            setupRobotModules()
        }
    }

    private fun initializeRobotSDK() {
        CsjRobot.authentication(this, "1b0bb9fa-b9a2-4e37-88e6-410ab33670c0",
            "D89E442C9C1082919A38B7F5CB9A45D8", object : OnAuthenticationListener {
            override fun success() {
                Log.d("TAG", "Authorization succeeded!")

            }

            override fun error() {
                Log.d("TAG", "Privilege grant failed!")
            }
        })
    }


    fun setupRobotModules() {
        CsjRobot.enableSlam(true)
        CsjRobot.getInstance().init(this)
        checkAndRequestOverlayPermission(this)

        // Chạy vòng lặp liên tục để cập nhật pin mỗi giây
        GlobalScope.launch {
            while (true) {
                delay(1000) // Cập nhật mỗi 1 giây
                CsjRobot.getInstance().getState().getBattery(object : OnRobotStateListener {
                    override fun getBattery(battery: Int) {
                        Log.d("TAG", "Battery level: $battery%")
                        _batteryLevel.value = battery
                    }

                    override fun getCharge(charge: Int) {
                        Log.d("TAG", "Charge state: $charge")
                        _chargeState.value = charge == 1
                    }
                })
            }
        }
    }


    private fun checkSelfStatus() {
        Log.d("TAG", "Checking self status...")

        // Gọi hàm checkSelf và xử lý kết quả thông qua listener

    }


    suspend fun openDoors() {
        CsjRobot.getInstance().state.closeOneFloorDoor()
        Log.d("hhhhhh", "Đã đóng")
        delay(1000);
        CsjRobot.getInstance().getState().openOneFloorDoor()
        Log.d("hhhhhh", "Đã mở 1")
        delay(1000);
        CsjRobot.getInstance().state.openTwoFloorDoor()
        Log.d("hhhhhh", "Đã mo cua 2")
        delay(1000);
        CsjRobot.getInstance().state.doubleDoorCloseControl()


//        CsjRobot.getInstance().getState().openTwoFloorDoor()
//        CsjRobot.getInstance().getState().doubleDoorOpenControl()
//        CsjRobot.getInstance().getState().doubleDoorCloseControl()
    }

    private fun initializeSharedPreferences() {
        SharedPreferencesSDCard.init(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun checkAndRequestOverlayPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
                context.startActivity(intent)
            }
        }
    }
}

