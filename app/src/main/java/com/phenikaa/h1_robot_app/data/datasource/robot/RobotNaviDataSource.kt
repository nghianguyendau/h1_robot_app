package com.phenikaa.h1_robot_app.data.datasource.robot

import android.util.Log
import com.csjbot.coshandler.core.CsjRobot
import com.csjbot.coshandler.core.State
import com.csjbot.coshandler.listener.OnGoRotationListener
import com.csjbot.coshandler.listener.OnNaviListener
import com.csjbot.coshandler.listener.OnPositionListener
import com.csjbot.coshandler.listener.OnSpeedGetListener
import com.google.gson.Gson
import com.phenikaa.h1_robot_app.data.model.RosPosition
import org.json.JSONObject
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RobotNaviDataSource @Inject constructor(
    private val robot: CsjRobot,
    private val gson: Gson
) {
    private val robotAction = robot.getAction()

//    suspend fun getCurrentPosition(): RosPosition = suspendCoroutine { continuation ->
//        robotAction.getPosition(object : OnPositionListener {
//            override fun positionInfo(json: String?) {
//                Log.d("RobotNaviDataSource", "Position info: $json")
//                try {
//                    val position = gson.fromJson(json, RosPosition::class.java)
//                    continuation.resume(position)
//                } catch (e: Exception) {
//                    continuation.resumeWithException(e)
//                }
//            }
//        })
//    }

    // Lấy vị trí hiện tại của robot
    suspend fun getCurrentPosition(): RosPosition = suspendCoroutine { continuation ->
        robotAction.getPosition(object : OnPositionListener {
            override fun positionInfo(json: String?) {
                try {
                    val jsonObject = JSONObject(json)
                    val position = RosPosition(
                        pos = RosPosition.PosBean(
                            x = jsonObject.getDouble("x").toFloat(),
                            y = jsonObject.getDouble("y").toFloat(),
                            z = jsonObject.getDouble("z").toFloat(),
                            rotation = jsonObject.getDouble("rotation").toFloat()
                        )
                    )
                    continuation.resume(position)
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }
        })
    }

    // Điều hướng đến một vị trí
    suspend fun navigateToPosition(position: RosPosition): Boolean = suspendCoroutine { continuation ->
        // Biến cờ để kiểm soát việc gọi resume
        var isResumed = false

        try {
            val jsonObject = JSONObject()
            jsonObject.put("msg_id", "NAVI_ROBOT_MOVE_TO_REQ")
            jsonObject.put("x", position.pos.x)
            jsonObject.put("y", position.pos.y)
            jsonObject.put("z", position.pos.z)
            jsonObject.put("rotation", position.pos.rotation)

            val json = jsonObject.toString()

            robotAction.navi(json, object : OnNaviListener {
                override fun moveResult(result: String) {
                    // Đảm bảo chỉ gọi resume một lần
                    if (!isResumed) {
                        isResumed = true
                        if (result.contains("\"error_code\":0")) {
                            continuation.resume(true) // Thành công
                        } else {
                            continuation.resume(false) // Thất bại
                        }
                    }
                }

                override fun messageSendResult(result: String) {
                    Log.d("RobotNaviDataSource", "Message sent result: $result")
                }

                override fun cancelResult(result: String) {
                    // Đảm bảo chỉ gọi resume một lần
                    if (!isResumed) {
                        isResumed = true
                        continuation.resume(false) // Điều hướng bị hủy
                    }
                }

                override fun goHome() {
                    // Không cần xử lý trong trường hợp này
                }
            })
        } catch (e: Exception) {
            // Đảm bảo chỉ gọi resumeWithException một lần
            if (!isResumed) {
                isResumed = true
                continuation.resumeWithException(e)
            }
        }
    }



    suspend fun goAngle(angle: Int){
        robotAction.goAngle(angle)
    }
    suspend fun moveAngle(angle: Int): Boolean = suspendCoroutine { continuation ->
        robotAction.moveAngle(angle, object : OnGoRotationListener {
            override fun response(responseAngle: Int) {
                try {
                    if (responseAngle > 0 && responseAngle < 360) {
                        if (responseAngle <= 180) {
                            if (robot.getState().getChargeState() == State.NOT_CHARGING) {
                                robotAction.moveAngle(responseAngle, null)
                                continuation.resume(true) // Thành công
                            }
                        } else {
                            if (robot.getState().getChargeState() == State.NOT_CHARGING) {
                                robotAction.moveAngle(-(360 - responseAngle), null)
                                continuation.resume(true) // Thành công
                            }
                        }
                    } else {
                        continuation.resume(false) // Góc không hợp lệ
                    }
                } catch (e: Exception) {
                    continuation.resumeWithException(e) // Báo lỗi
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

//    suspend fun navigateToPosition(position: RosPosition): Boolean = suspendCoroutine { continuation ->
//        val json = gson.toJson(position)
//        robotAction.navi(json, object : OnNaviListener {
//            override fun moveResult(result: String) {
//                try {
//                    val status = when {
//                        result.contains("\"status\":0") || result.contains("\"code\":0") -> true
//                        else -> false
//                    }
//                    continuation.resume(status)
//                } catch (e: Exception) {
//                    continuation.resumeWithException(e)
//                }
//            }
//
//            override fun messageSendResult(result: String) {
//                // Xử lý kết quả gửi message nếu cần
//            }
//
//            override fun cancelResult(result: String) {
//                // Xử lý kết quả cancel nếu cần
//            }
//
//            override fun goHome() {
//                // Xử lý sự kiện go home nếu cần
//            }
//        })
//    }





    suspend fun goHome(): Boolean = suspendCoroutine { continuation ->
        robotAction.goHome(object : OnNaviListener {
            override fun moveResult(result: String) {
                if (result.contains("\"status\":0") || result.contains("\"code\":0")) {
                    continuation.resume(true) // Thành công
                } else {
                    continuation.resume(false) // Thất bại
                }
            }

            override fun messageSendResult(result: String) {}
            override fun cancelResult(result: String) {}
            override fun goHome() {}
        })
    }

    fun cancelNavigation(listener: OnNaviListener?) {
        robotAction.cancelNavi(listener)
    }

    fun moveDirection(direction: Int) {
        robotAction.move(direction)
    }

    fun moveBySerial(direction: Int){
        robotAction.moveBySerial(direction)
    }

    suspend fun moveSerial(linear: Int, angular: Int): Boolean = suspendCoroutine { continuation ->
        try {
            robotAction.moveSerial(linear, angular)
            continuation.resume(true)
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }

    fun setSpeed(speed: Float) {
        robotAction.setSpeed(speed)
    }

//    suspend fun isDestinationReachable(position: RosPosition): Boolean =
//        suspendCoroutine { continuation ->
//            robotAction.destReachable(
//                position.x,
//                position.y,
//                position.rotation
//            ) { isReachable ->
//                continuation.resume(isReachable)
//            }
//        }
}