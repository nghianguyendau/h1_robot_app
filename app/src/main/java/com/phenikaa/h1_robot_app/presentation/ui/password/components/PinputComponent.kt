package com.phenikaa.h1_robot_app.presentation.ui.password.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phenikaa.h1_robot_app.domain.entity.enum.PinPasswordStatus
import com.phenikaa.h1_robot_app.presentation.theme.AppColor

@Composable
fun PinPutComponent(
    listPin: List<String>,
    pinLength: Int,
    pinPassWordStatus: PinPasswordStatus
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)

    ) {
        repeat(pinLength) { index ->
            if (listPin.size > index) Pin(
                value = listPin[index],
            pinPassWordStatus
            ) else Pin(value = "", PinPasswordStatus.DISABLE)
        }
    }
}


@Composable
fun Pin(
    value: String,
    pinPassWordStatus: PinPasswordStatus
) {
    Box(
        modifier = Modifier
            .height(100.dp)
            .width(100.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AppColor.neutral100)
            .border(
                1.dp,
                when (pinPassWordStatus) {
                    PinPasswordStatus.DISABLE -> Color.Transparent
                    PinPasswordStatus.ENABLE -> AppColor.blue400
                    PinPasswordStatus.FALSE -> AppColor.red400
                    else -> {
                        Color.Transparent
                    }
                },
                RoundedCornerShape(14.dp)
            )
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AppColor.blackColor,
            textAlign = TextAlign.Center,
        )
    }
}