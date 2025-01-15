package com.phenikaa.h1_robot_app.presentation.features.navigation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phenikaa.h1_robot_app.data.model.RosPosition
import com.phenikaa.h1_robot_app.domain.model.NavigationState
import com.phenikaa.h1_robot_app.domain.model.Position

@Composable
fun NavigationScreen(
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val navigationState by viewModel.navigationState.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val navigationResult by viewModel.navigationResult.collectAsState()

    val currentSpeed by viewModel.currentSpeed.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentPosition()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Current Position Display
        currentPosition?.let { position ->
            Text(
                text = "Current Position:",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "X: ${position.x}, Y: ${position.y}, Rotation: ${position.rotation}°"
            )
        } ?: Text("Fetching current position...")

        // Navigation State Display
        when (navigationState) {
            is NavigationState.Navigating -> {
                CircularProgressIndicator()
                Text("Navigating...")
            }
            is NavigationState.Completed -> {
                Text("Navigation completed successfully")
            }
            is NavigationState.Error -> {
                Text(
                    text = (navigationState as NavigationState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is NavigationState.Idle -> {
                Text("Ready to navigate")
            }
        }

        // Navigation Controls
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DirectionButton(
                    text = "Turn Left",
                    onClick = {
                        viewModel.moveDirection(2)
                    }
                )
                DirectionButton(
                    text = "Turn Right",
                    onClick = {
                        viewModel.moveDirection(3)
                    }
                )
                DirectionButton(
                    text = "Forward",
                    onClick = {
                        viewModel.moveDirection(0)
                    }
                )
                DirectionButton(
                    text = "Backward",
                    onClick = {
                        viewModel.moveDirection(1)
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DirectionButton(
                    text = "Move Forward",
                    onClick = { viewModel.moveBySerial(0x01) }
                )
                DirectionButton(
                    text = "Turn Left",
                    onClick = { viewModel.moveBySerial(0x03) }
                )
                DirectionButton(
                    text = "Turn Right",
                    onClick = { viewModel.moveBySerial(0x04) }
                )
                DirectionButton(
                    text = "Move Forward",
                    onClick = { viewModel.moveBySerial(0x02) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DirectionButton(
                    text = "Rotate 60",
                    onClick = { viewModel.goAngle(60) }
                )
                DirectionButton(
                    text = "Rotate 120",
                    onClick = { viewModel.goAngle(120) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DirectionButton(
                    text = "Rotate 90°",
                    onClick = { viewModel.moveAngle(90) } // Quay 90 độ
                )
                DirectionButton(
                    text = "Rotate 270°",
                    onClick = { viewModel.moveAngle(270) } // Quay 270 độ
                )
            }

//            Row {
//                DirectionButton(
//                    text = "Navigate to (1.0, 2.0, 0.0)",
//                    onClick = {
//                        val position = RosPosition(
//                            poseName = "Docking Station",
//                            pos = RosPosition.PosBean(
//                                x = 1.0f,
//                                y = 2.0f,
//                                z = 0.0f,
//                                rotation = 90.0f
//                            )
//                        )
//                        viewModel.navigateToPosition(position)
//                    }
//                )
//            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DirectionButton(
                    text = "Save Position",
                    onClick = { viewModel.saveCurrentPosition() }
                )
                DirectionButton(
                    text = "Go",
                    onClick = {  viewModel.navigateToSavedPosition()  }
                )

                DirectionButton(
                    text = "Go home",
                    onClick = {
                        viewModel.goHome()
                    }
                )
                DirectionButton(
                    text = "Cancel navi",
                    onClick = {
                        viewModel.cancelNavi()
                    }
                )
                // Hiển thị kết quả điều hướng
                when (navigationResult) {
                    true -> Text("Navigation completed successfully!")
                    false -> Text("Navigation failed.")
                    null -> Text("Ready to navigate.")
                }
            }

            Row {
                // Hiển thị tốc độ hiện tại
                Text(
                    text = currentSpeed?.let { "Current Speed: $it m/s" } ?: "Speed not available",
                    style = MaterialTheme.typography.titleMedium
                )
                DirectionButton(
                    text = "Get current speed",
                    onClick = {
                        viewModel.fetchCurrentSpeed()
                    }
                )
                DirectionButton(
                    text = "Set speed 0.8 m/s",
                    onClick = {
                        viewModel.setSpeed(0.8f)
                    }
                )
                DirectionButton(
                    text = "Set speed 0.2 m/s",
                    onClick = {
                        viewModel.setSpeed(0.2f)
                    }
                )

            }
        }
    }
}

@Composable
private fun DirectionButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text)
    }
}