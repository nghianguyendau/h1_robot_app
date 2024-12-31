package com.phenikaa.h1_robot_app.data.datasource.robot

import android.util.Log
import com.csjbot.coshandler.core.CsjRobot
import com.csjbot.coshandler.listener.OnNaviListener
import com.csjbot.coshandler.listener.OnPositionListener
import com.csjbot.coshandler.listener.OnSpeedGetListener
import com.google.gson.Gson
import com.phenikaa.h1_robot_app.data.model.RosPosition
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RobotNaviDataSource @Inject constructor(
    private val robot: CsjRobot,
    private val gson: Gson
) {
    private val robotAction = robot.getAction()

    suspend fun getCurrentPosition(): RosPosition = suspendCoroutine { continuation ->
        robotAction.getPosition(object : OnPositionListener {
            override fun positionInfo(json: String?) {
                Log.d("RobotNaviDataSource", "Position info: $json")
                try {
                    val position = gson.fromJson(json, RosPosition::class.java)
                    continuation.resume(position)
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }
        })
    }

    suspend fun getSpeed(): Float = suspendCoroutine { continuation ->
       robotAction.getSpeed(object : OnSpeedGetListener {
           override fun getNaviSpeed(p0: Double) {
               try {
                     continuation.resume(p0.toFloat())
                } catch (e: Exception) {
                     continuation.resumeWithException(e)
               }
           }
       })
    }

    suspend fun navigateToPosition(position: RosPosition): Boolean = suspendCoroutine { continuation ->
        val json = gson.toJson(position)
        robotAction.navi(json, object : OnNaviListener {
            override fun moveResult(result: String) {
                try {
                    val status = when {
                        result.contains("\"status\":0") || result.contains("\"code\":0") -> true
                        else -> false
                    }
                    continuation.resume(status)
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }

            override fun messageSendResult(result: String) {
                // Xử lý kết quả gửi message nếu cần
            }

            override fun cancelResult(result: String) {
                // Xử lý kết quả cancel nếu cần
            }

            override fun goHome() {
                // Xử lý sự kiện go home nếu cần
            }
        })
    }

    fun cancelNavigation() {
        robotAction.cancelNavi(null)
    }

    fun moveDirection(direction: Int) {
        robotAction.move(direction)
    }

    fun setSpeed(speed: Float) {
        robotAction.setSpeed(speed)
    }

    suspend fun isDestinationReachable(position: RosPosition): Boolean =
        suspendCoroutine { continuation ->
            robotAction.destReachable(
                position.x,
                position.y,
                position.rotation
            ) { isReachable ->
                continuation.resume(isReachable)
            }
        }
}