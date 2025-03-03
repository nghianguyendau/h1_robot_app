package com.phenikaa.h1_robot_app.presentation.ui.robot_route

import com.phenikaa.h1_robot_app.domain.entity.RobotRoute


data class RobotRouteState(
    val robotRoute: RobotRoute = RobotRoute(),
    val test: String = "Toan"
)

sealed class RobotRouteStateNavigationState {
    data object NavigationBar : RobotRouteStateNavigationState()
}