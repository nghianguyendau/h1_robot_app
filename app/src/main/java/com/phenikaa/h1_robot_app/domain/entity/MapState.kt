package com.phenikaa.h1_robot_app.domain.entity

sealed interface MapState {
    data class Available(val mapList: List<String>) : MapState
    object Loading : MapState
    data class Error(val message: String) : MapState
}