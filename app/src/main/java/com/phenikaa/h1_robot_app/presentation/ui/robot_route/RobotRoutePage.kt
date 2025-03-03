package com.phenikaa.h1_robot_app.presentation.ui.robot_route


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.phenikaa.h1_robot_app.presentation.common_view.CommonScaffold
import com.phenikaa.h1_robot_app.presentation.theme.AppColor
import com.phenikaa.h1_robot_app.presentation.ui.password.PassWordState

@Composable
fun RobotRoutePage (
    navController: NavHostController,
    lPointId: List<Int> = listOf(114),
    viewModel: RobotRouteViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.onInitRobotRoute(lPointId)
    }

    val state by viewModel.state.collectAsState()
    RobotRouteScreen(state,navController)
}

@Composable
fun RobotRouteScreen (
    state: RobotRouteState,
    appNavController: NavHostController,
) {
   CommonScaffold(
       content = { paddingValues ->
           Column {
                Text(text = state.robotRoute.status,
                    color = AppColor.red400)
           }

   }, navController = appNavController)
}