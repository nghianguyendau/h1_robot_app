package com.phenikaa.h1_robot_app.presentation.features.phonecall

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.phenikaa.h1_robot_app.data.datasource.websocket.ConnectionState

@Composable
fun PhoneCallScreen(
    navController: NavHostController,
    viewModel: PhoneCallViewModel = hiltViewModel()
) {
    var roomNumber by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val connectionState by viewModel.connectionState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val trayState by viewModel.trayState.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()

    LaunchedEffect(connectionState) {
        if (connectionState == ConnectionState.DISCONNECTED) {
            Log.d("PhoneCallScreen", "Reconnecting WebSocket")
            viewModel.connect("ws://192.168.99.176:8080")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.disconnect()
        }
    }

    when (val currentState = uiState) {
        is PhoneCallUiState.EnterRoomNumber -> {
            EnterNumberScreen(
                connectionState = connectionState,
                number = roomNumber,
                headerText = "Vui lòng nhập số phòng của khách",
                buttonText = "Tiếp",
                onNumberClick = { number ->
                    roomNumber += number
                },
                onBackSpaceClick = {
                    roomNumber = roomNumber.dropLast(1)
                },
                onConfirmClick = {
                    viewModel.setRoomNumber(roomNumber)
                    viewModel.updateState(PhoneCallUiState.EnterPhoneNumber)
                },
                isConfirmEnabled = roomNumber.isNotEmpty()
            )
        }

        is PhoneCallUiState.EnterPhoneNumber -> {
            EnterNumberScreen(
                connectionState = connectionState,
                number = phoneNumber,
                headerText = "Vui lòng nhập số điện thoại của khách hàng",
                buttonText = "OK",
                onNumberClick = { number ->
                    phoneNumber += number
                },
                onBackSpaceClick = {
                    phoneNumber = phoneNumber.dropLast(1)
                },
                onConfirmClick = {
                    viewModel.setPhoneNumber(phoneNumber)
                    viewModel.updateState(PhoneCallUiState.MovingToRoomNumber(roomNumber))
                },
                isConfirmEnabled = phoneNumber.length >= 10
            )
        }

        is PhoneCallUiState.MovingToRoomNumber -> {
            MovingToRoomNumberScreen(
                roomNumber = currentState.roomNumber,
                onArrived = {
                    if(connectionState == ConnectionState.CONNECTED) {
                        viewModel.updateState(PhoneCallUiState.OnCall(CallStatus.Calling))
                        viewModel.makePhoneCall()
                    }
                },
            )
        }

        is PhoneCallUiState.OnCall -> {
            CallingScreen(
                callStatus = currentState.status,
                trayState = trayState,
                phoneNumber = viewModel.getPhoneNumber(),
                timeLeft = timeLeft,
                onOpenTray = { viewModel.openTray() },
                onConfirm = {
                    viewModel.reset()
                    navController.navigate("home") {
                        popUpTo("phoneCall") { inclusive = true }
                    }
                }
            )
        }

        is PhoneCallUiState.Error -> {
            ErrorScreen(
//                onConfirm = {
//                    viewModel.reset()
//                    navController.navigate("home") {
//                        popUpTo("phoneCall") { inclusive = true }
//                    }
//                }
            )
        }
    }
}