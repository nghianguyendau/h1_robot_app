package com.phenikaa.h1_robot_app.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun CustomSpacer(height: Int, width: Int) {
    Spacer(
        modifier = androidx.compose.ui.Modifier
            .height(height.dp)
            .width(width.dp)
    )
}