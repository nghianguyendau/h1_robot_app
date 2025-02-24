package com.phenikaa.h1_robot_app.presentation.ui.password

import com.phenikaa.h1_robot_app.domain.entity.enum.PinPasswordStatus

data class PassWordState(
    val lPassWord: List<String> = emptyList(),
    val lengthPassWord: Int = 6,
    val pinStatus: PinPasswordStatus = PinPasswordStatus.DISABLE,
    val passWord: String = ""
)

sealed class PassWordNavigationState {
    data object NavigationBar : PassWordNavigationState()
}