package com.phenikaa.h1_robot_app.presentation.features.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phenikaa.h1_robot_app.domain.model.NavigationState

@Composable
fun NavigationScreen(
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val navigationState by viewModel.navigationState.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()

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
        }

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DirectionButton(
                text = "Forward",
                onClick = { /* Implement forward movement */ }
            )
            DirectionButton(
                text = "Backward",
                onClick = { /* Implement backward movement */ }
            )
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