package com.phenikaa.h1_robot_app.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.phenikaa.h1_robot_app.presentation.ui.cruising.CruisingScreen
import com.phenikaa.h1_robot_app.presentation.ui.elevator.RobotElevatorScreen
import com.phenikaa.h1_robot_app.presentation.ui.home.HomeScreen
import com.phenikaa.h1_robot_app.presentation.ui.navigation.NavigationScreen
import com.phenikaa.h1_robot_app.presentation.ui.phonecall.PhoneCallScreen
import com.phenikaa.h1_robot_app.presentation.ui.robotdoor.RobotDoorScreen
import com.phenikaa.h1_robot_app.presentation.ui.saveposition.SavePositionScreen
import com.phenikaa.h1_robot_app.presentation.ui.websocket.WebSocketScreen
import com.phenikaa.h1_robot_app.presentation.common_view.CustomTopAppBar
import com.phenikaa.h1_robot_app.presentation.ui.password.PassWordPage
import com.phenikaa.h1_robot_app.presentation.ui.robot_route.RobotRoutePage


@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // Theo dõi route hiện tại
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    Column {
        CustomTopAppBar(
            showBack = currentRoute != Screen.Home.route,
            onBackClick = { navController.navigateUp() }
        )

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                  navController = navController
                )
            }
            composable(Screen.Navigation.route) {
                NavigationScreen(navController)
            }
            composable(Screen.WebSocket.route) {
                WebSocketScreen(navController)
            }
            composable(Screen.PhoneCall.route) {
                PhoneCallScreen(
                    navController = navController
                )
            }
            composable(Screen.DoorScreen.route) {
                RobotDoorScreen(navController)
            }
            composable(Screen.ElevatorScreen.route) {
                RobotElevatorScreen(navController)
            }
            composable(Screen.SavePositionScreen.route) {
                SavePositionScreen(navController)
            }
            composable(Screen.CruisingScreen.route) {
                CruisingScreen(navController)
            }
            composable(Screen.PassWordPage.route) {
                PassWordPage(navController)
            }
            composable(Screen.RobotRoutePage.route) {
                RobotRoutePage(navController)
            }
        }
    }
}