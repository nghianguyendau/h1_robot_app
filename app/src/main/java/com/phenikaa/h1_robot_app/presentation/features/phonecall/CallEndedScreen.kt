package com.phenikaa.h1_robot_app.presentation.features.phonecall

import androidx.compose.runtime.Composable
import com.phenikaa.h1_robot_app.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CallEndedScreen(onConfirm: () -> Unit) {
    var timeLeft by remember { mutableStateOf(140) }
    var showOpenTrayButton by remember { mutableStateOf(true) }
    var isOpeningTray by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.robot_img),
                contentDescription = "Delivery Robot",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            )

            if (isOpeningTray) {
                Text(
                    text = "Đang mở khay...",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                LaunchedEffect(Unit) {
                    delay(5000)
                    isOpeningTray = false
                    showOpenTrayButton = false
                }
            } else if (showOpenTrayButton) {
                Button(
                    onClick = {
                        isOpeningTray = true
                    },
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFA5)
                    )
                ) {
                    Text(
                        text = "Mở khay",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else {
                Button(
                    onClick = { onConfirm() },
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00BFA5)
                    )
                ) {
                    Text(
                        text = "Xác nhận",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }

        Text(
            text = "Thời gian lấy hàng: ${timeLeft}s",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 32.dp, bottom = 24.dp),
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp
        )
    }
}
