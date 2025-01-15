package com.phenikaa.h1_robot_app.presentation.features.websocket

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phenikaa.h1_robot_app.data.datasource.websocket.ConnectionState

@Composable
fun WebSocketScreen(
    viewModel: WebSocketViewModel = hiltViewModel()
) {
    val message by viewModel.message.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Connection State Display
        Text(
            text = when (connectionState) {
                ConnectionState.CONNECTED -> "Connected"
                ConnectionState.DISCONNECTED -> "Disconnected"
            },
            color = if (connectionState == ConnectionState.CONNECTED) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )

        // Messages Display
        Text(
            text = message?.toString() ?: "No messages yet",
            modifier = Modifier.padding(16.dp)
        )

        // Input and Send Button
        Row(modifier = Modifier.padding(16.dp)) {
            var input by remember { mutableStateOf("") }

            TextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                viewModel.sendMessage(input, "0853101858")
                input = ""
            }) {
                Text("Send")
            }
        }
    }

    // Ensuring connection on screen load
    LaunchedEffect(Unit) {
        viewModel.connect("ws://192.168.99.176:8080")
    }
}
