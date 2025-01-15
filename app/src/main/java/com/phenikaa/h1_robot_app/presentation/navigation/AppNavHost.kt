package com.phenikaa.h1_robot_app.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.phenikaa.h1_robot_app.presentation.features.home.HomeScreen
import com.phenikaa.h1_robot_app.presentation.features.navigation.NavigationScreen
import com.phenikaa.h1_robot_app.presentation.features.phonecall.PhoneCallScreen
import com.phenikaa.h1_robot_app.presentation.features.robotdoor.RobotDoorScreen
import com.phenikaa.h1_robot_app.presentation.features.websocket.WebSocketScreen
import com.phenikaa.h1_robot_app.ui.components.CustomTopAppBar

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
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
                NavigationScreen()
            }
            composable(Screen.WebSocket.route) {
                WebSocketScreen()
            }
            composable(Screen.PhoneCall.route) {
                PhoneCallScreen(
                    navController = navController
                )
            }
            composable(Screen.DoorScreen.route) {
                RobotDoorScreen()
            }
        }
    }
}