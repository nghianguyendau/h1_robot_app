package com.phenikaa.h1_robot_app.presentation.ui.robot_route

import android.util.Log
import com.phenikaa.h1_robot_app.data.model.RobotRouteData
import com.phenikaa.h1_robot_app.domain.entity.RobotRoute
import com.phenikaa.h1_robot_app.domain.usecase.GetPassWordUseCase
import com.phenikaa.h1_robot_app.domain.usecase.InitPassWordAppUseCase
import com.phenikaa.h1_robot_app.domain.usecase.robot_route.RobotRouteConnectUseCase
import com.phenikaa.h1_robot_app.domain.usecase.robot_route.RobotRouteReceiveMessagesUseCase
import com.phenikaa.h1_robot_app.domain.usecase.robot_route.RobotRouteSendMessengerUseCase
import com.phenikaa.h1_robot_app.domain.usecase.robot_route.robotRouteNaviPosUseCase
import com.phenikaa.h1_robot_app.presentation.base.BaseViewModel
import com.phenikaa.h1_robot_app.presentation.ui.password.PassWordNavigationState
import com.phenikaa.h1_robot_app.utils.singleSharedFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RobotRouteViewModel @Inject constructor(
    private val robotRouteConnectUseCase: RobotRouteConnectUseCase,
    private val robotRouteSendMessengerUseCase: RobotRouteSendMessengerUseCase,
    private val robotRouteReceiveMessagesUseCase: RobotRouteReceiveMessagesUseCase,
    private val robotRouteNaviPosUseCase: robotRouteNaviPosUseCase,
) : BaseViewModel() {
    private val _uiState: MutableStateFlow<RobotRouteState> = MutableStateFlow(RobotRouteState())
    val state = _uiState.asStateFlow()

    private val _navigationState: MutableSharedFlow<PassWordNavigationState> = singleSharedFlow()
    val navigationState = _navigationState.asSharedFlow()


    fun onInitRobotRoute(lPointId: List<Int> ) = launch {
        robotRouteConnectUseCase.invoke()
        robotRouteSendMessengerUseCase.invoke("route_analyze",lPointId)
        onGetRobotRoute()
        onRobotRouteNaviPos()
    }

    suspend fun onGetRobotRoute() {
        val robotRoute = robotRouteReceiveMessagesUseCase.invoke()
        _uiState.update {
                it.copy(robotRoute = robotRoute)
            }
        Log.d("RobotRouteNavi", "onGetRobotRoute")
    }

    suspend fun onRobotRouteNaviPos() {
        robotRouteNaviPosUseCase.invoke(_uiState.value.robotRoute.robotRouteTask.data.first().navigationSteps.first().robotRoutePose)
    }


}

