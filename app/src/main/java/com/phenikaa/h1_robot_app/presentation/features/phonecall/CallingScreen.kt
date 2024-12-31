package com.phenikaa.h1_robot_app.presentation.features.phonecall

import com.phenikaa.h1_robot_app.R
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CallingScreen(phoneNumber: String) {
    val infiniteTransition = rememberInfiniteTransition()

    // Tăng số lượng sóng và giảm độ trễ để tạo hiệu ứng liên tục
    val waves = (0..4).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,  // Tăng thời gian để sóng lan ra xa hơn
                    delayMillis = (index * 400),  // Giảm độ trễ giữa các sóng
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Đang gọi: $phoneNumber",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Box(
            modifier = Modifier
                .size(450.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = size.width / 2
                val iconSize = 120.dp.toPx()
                val iconRadius = iconSize / 2

                waves.forEach { animatedValue ->
                    val scale = animatedValue.value
                    val waveSize = (center - iconRadius) * scale * 2 + iconSize
                    val offset = center - (waveSize / 2)

                    // Left wave
                    drawArc(
                        color = Color(0xFF6cdc2c).copy(
                            alpha = (0.6f * (1f - scale)).coerceIn(0f, 0.6f)  // Điều chỉnh độ trong suốt
                        ),
                        startAngle = -60f,
                        sweepAngle = 120f,
                        useCenter = false,
                        topLeft = Offset(offset, offset),
                        size = Size(waveSize, waveSize),
                        style = Stroke(
                            width = 4.dp.toPx(),  // Giảm độ dày của sóng
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.cornerPathEffect(10f)
                        )
                    )

                    // Right wave
                    drawArc(
                        color = Color(0xFF6cdc2c).copy(
                            alpha = (0.6f * (1f - scale)).coerceIn(0f, 0.6f)
                        ),
                        startAngle = 120f,
                        sweepAngle = 120f,
                        useCenter = false,
                        topLeft = Offset(offset, offset),
                        size = Size(waveSize, waveSize),
                        style = Stroke(
                            width = 4.dp.toPx(),
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.cornerPathEffect(10f)
                        )
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.phone_call),
                contentDescription = "Phone Icon",
                modifier = Modifier.size(120.dp)
            )
        }
    }
}