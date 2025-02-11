package com.phenikaa.h1_robot_app.presentation.features.elevator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phenikaa.h1_robot_app.presentation.features.navigation.NavigationViewModel
import kotlinx.coroutines.launch

@Composable
fun RobotElevatorScreen(
    robotElevatorViewModel: ElevatorViewModel = hiltViewModel(),
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val robotState by robotElevatorViewModel.robotState.collectAsState()
    val messages by robotElevatorViewModel.messages.collectAsState()
    val currentPosition by navigationViewModel.currentPosition.collectAsState()

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    // Truyền NavigationViewModel trực tiếp vào RobotElevatorViewModel
    LaunchedEffect(Unit) {
        robotElevatorViewModel.navigationViewModel = navigationViewModel
        navigationViewModel.getCurrentPosition()
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                lazyListState.scrollToItem(messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
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
        Text(text = "Robot State: $robotState", style = MaterialTheme.typography.bodyMedium)

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.weight(1f).padding(vertical = 8.dp)
        ) {
            items(messages) { message ->
                Text(text = message, modifier = Modifier.padding(4.dp))
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
//            Button(onClick = { robotElevatorViewModel.callElevator(13, 1) }) {
//                Text("Call Elevator 12A-1")
//            }
            Button(onClick = { robotElevatorViewModel.callElevatorDown() }) {
                Text("Call Elevator 12A-1")
            }
            Button(onClick = { robotElevatorViewModel.callElevatorUp() }) {
                Text("Call Elevator 1-12A")
            }
            Button(onClick = { robotElevatorViewModel.cancelAllTasks() }) {
                Text("Cancel task")
            }

//            Button(onClick = { robotElevatorViewModel.sendRobotConfirmation("event_1234") }) {
//                Text("Send Confirmation")
//            }
//            Button(onClick = { robotElevatorViewModel.saveCurrentPosition1()}) {
//                Text("Save Position 1")
//            }
//            Button(onClick = { robotElevatorViewModel.saveCurrentPosition2()}) {
//                Text("Save Position 2")
//            }
//            Button(onClick = { robotElevatorViewModel.navigateToCabin()}) {
//                Text("Go to saveP2")
//            }
//            Button(onClick = { robotElevatorViewModel.navigateToElevatorPoint()}) {
//                Text("Go to saveP1")
//            }
            Button(onClick = { navigationViewModel.goHome()}) {
                Text("Go home")
            }
        }
    }
}

