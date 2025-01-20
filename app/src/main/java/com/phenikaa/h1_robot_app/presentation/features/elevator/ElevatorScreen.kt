package com.phenikaa.h1_robot_app.presentation.features.elevator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RobotElevatorScreen(viewModel: RobotElevatorViewModel = hiltViewModel()) {
    val robotState by viewModel.robotState.collectAsState()
    val messages by viewModel.messages.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Robot State: $robotState", style = MaterialTheme.typography.bodyMedium)

        LazyColumn(
            modifier = Modifier.weight(1f).padding(vertical = 8.dp)
        ) {
            items(messages) { message ->
                Text(text = message, modifier = Modifier.padding(4.dp))
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { viewModel.callElevator() }) {
                Text("Call Elevator")
            }

            Button(onClick = { viewModel.sendRobotConfirmation("event_1234") }) {
                Text("Send Confirmation")
            }
        }
    }
}
