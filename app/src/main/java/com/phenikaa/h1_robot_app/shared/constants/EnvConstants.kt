package com.phenikaa.h1_robot_app.shared.constants

class EnvConstants private constructor(){
    companion object {
        const val ROUTE_WEBSOCKET_URL = "wss://relative-nearly-polecat.ngrok-free.app?type=ROBOT&serial_number=SN01"
        const val KONE_ELEVATOR_WEBSOCKET_URL = "wss://dev.kone.com/stream-v2?accessToken="
    }
}