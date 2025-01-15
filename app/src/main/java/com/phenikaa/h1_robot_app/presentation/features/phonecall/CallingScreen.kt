package com.phenikaa.h1_robot_app.presentation.features.phonecall

import com.phenikaa.h1_robot_app.R
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.phenikaa.h1_robot_app.ui.components.CustomSpacer
import kotlinx.coroutines.delay

@Composable
fun CallingScreen(
    callStatus: CallStatus,
    trayState: DeliveryTrayState,
    phoneNumber: String,
    timeLeft: Int,
    onOpenTray: () -> Unit,
    onConfirm: () -> Unit
) {
    // Infinite transition for waves animation
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val waves = (0..4).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,
                    delayMillis = index * 400,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = ""
        )
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Robot image + Countdown timer
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.robot_img)
                    .crossfade(true)
                    .scale(Scale.FIT)
                    .transformations(RoundedCornersTransformation(16f))
                    .size(600)
                    .build(),
                contentDescription = "Delivery Robot",
                modifier = Modifier
                    .size(600.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Thời gian lấy hàng: ${timeLeft}s",
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        }

        // Right side: Call status, waves, and buttons
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Call status
            Text(
                text = when (callStatus) {
                    is CallStatus.Calling -> "Đang gọi: $phoneNumber"
                    is CallStatus.Ended -> "Cuộc gọi kết thúc"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Waves animation for ongoing call
            if (callStatus is CallStatus.Calling) {
                Box(
                    modifier = Modifier
                        .size(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = size.width / 2
                        val iconSize = 100.dp.toPx()
                        val iconRadius = iconSize / 2

                        waves.forEach { animatedValue ->
                            val scale = animatedValue.value
                            val waveSize = (center - iconRadius) * scale * 2 + iconSize
                            val offset = center - (waveSize / 2)

                            drawArc(
                                color = Color(0xFF6cdc2c).copy(
                                    alpha = (0.6f * (1f - scale)).coerceIn(0f, 0.6f)
                                ),
                                startAngle = -60f,
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
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                if (trayState == DeliveryTrayState.Opening) {
                    Text(
                        text = "Đang mở khay...",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            when {
                trayState == DeliveryTrayState.Opening -> {
                }
                trayState == DeliveryTrayState.Opened || callStatus is CallStatus.Ended -> {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .fillMaxWidth()
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
                else -> {
                    Button(
                        onClick = onOpenTray,
                        enabled = callStatus is CallStatus.Calling,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00BFA5)
                        )
                    ) {
                        Text(
                            text = "Mở khay",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}
