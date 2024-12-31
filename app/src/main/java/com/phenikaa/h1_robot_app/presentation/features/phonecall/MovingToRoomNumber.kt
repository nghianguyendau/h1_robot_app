package com.phenikaa.h1_robot_app.presentation.features.phonecall

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phenikaa.h1_robot_app.ui.components.LottieAnimation

@Composable
fun MovingToRoomNumberScreen(
    roomNumber: String,
    onArrived: () -> Unit,
    onCall: () -> Unit
) {
    var isArrived by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(10000)
        isArrived = true

        kotlinx.coroutines.delay(5000)
        onArrived()
        onCall()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hiển thị văn bản dựa trên trạng thái
        Text(
            text = if (isArrived) "Đã đến phòng $roomNumber" else "Đang di chuyển đến phòng $roomNumber",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Hiển thị hiệu ứng Lottie
        LottieAnimation(
            modifier = Modifier.size(500.dp),
        )
    }
}