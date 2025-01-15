package com.phenikaa.h1_robot_app.ui.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.phenikaa.h1_robot_app.R

@Composable
fun LottieAnimation(
    modifier: Modifier = Modifier,
    rawResId: Int = R.raw.eyes_smile,
    iterations: Int = LottieConstants.IterateForever,
    speed: Float = 1f,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center
) {
    // Tải composition Lottie
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(rawResId)
    )

    // Điều khiển progress của animation
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        speed = speed
    )

    // Hiển thị animation

    com.airbnb.lottie.compose.LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = modifier
            .size(200.dp)
            .scale(1.2f)
            .offset(x = (-8).dp, y = 0.dp),
        contentScale = contentScale
    )
}
