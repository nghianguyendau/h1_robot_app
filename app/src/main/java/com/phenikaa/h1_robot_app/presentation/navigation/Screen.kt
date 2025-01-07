package com.phenikaa.h1_robot_app.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Navigation : Screen("navigation")
    object WebSocket : Screen("websocket")
    object PhoneCall: Screen("phone_call")
    object DoorScreen: Screen("door_screen")
}