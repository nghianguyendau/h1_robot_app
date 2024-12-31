package com.phenikaa.h1_robot_app.domain.model

sealed interface NavigationState {
    object Navigating : NavigationState
    object Completed : NavigationState
    data class Error(val message: String) : NavigationState
    object Idle : NavigationState
}