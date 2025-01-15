package com.phenikaa.h1_robot_app.presentation.features.phonecall

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phenikaa.h1_robot_app.R
import com.phenikaa.h1_robot_app.data.datasource.websocket.ConnectionState
import com.phenikaa.h1_robot_app.ui.components.CustomSpacer

@Composable
fun EnterNumberScreen(
    connectionState: ConnectionState,
    number: String,
    headerText: String,
    buttonText: String,
    onNumberClick: (String) -> Unit,
    onBackSpaceClick: () -> Unit,
    onConfirmClick: () -> Unit,
    isConfirmEnabled: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Trạng thái kết nối
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trạng thái: ",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            )

            Text(
                text = if (connectionState == ConnectionState.CONNECTED) "Sẵn sàng" else "Chưa sẵn sàng",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (connectionState == ConnectionState.CONNECTED) Color(0xFF4CAF50) else Color.Red
                )
            )
        }

        // Text hướng dẫn
        Text(
            text = headerText,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333)
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Hiển thị số được nhập
        Text(
            text = number.ifEmpty { "..." },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(bottom = 16.dp),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        // Bàn phím số
        NumericKeyboard(
            onNumberClick = onNumberClick,
            onBackSpaceClick = onBackSpaceClick,
            onConfirmClick = onConfirmClick,
            confirmButtonText = buttonText,
            isConfirmEnabled = isConfirmEnabled
        )
    }
}

@Composable
fun NumericKeyboard(
    onNumberClick: (String) -> Unit,
    onBackSpaceClick: () -> Unit,
    onConfirmClick: () -> Unit,
    confirmButtonText: String,
    isConfirmEnabled: Boolean
) {
    val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            numbers.slice(0..2).forEach { number ->
                KeyboardButton(
                    text = number,
                    onClick = { onNumberClick(number) }
                )
            }
        }

        CustomSpacer(16, 0)

        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            numbers.slice(3..5).forEach { number ->
                KeyboardButton(
                    text = number,
                    onClick = { onNumberClick(number) }
                )
            }
        }

        CustomSpacer(16, 0)

        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            numbers.slice(6..8).forEach { number ->
                KeyboardButton(
                    text = number,
                    onClick = { onNumberClick(number) }
                )
            }
        }

        CustomSpacer(16, 0)

        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            IconButton(
                onClick = onBackSpaceClick,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Red.copy(alpha = 0.5f))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.rounded_close_24),
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            KeyboardButton(
                text = "0",
                onClick = { onNumberClick("0") }
            )

            KeyboardButton(
                text = confirmButtonText,
                backgroundColor = if (isConfirmEnabled) Color(0xFF4CAF50) else Color.Gray,
                onClick = {
                    if (isConfirmEnabled) onConfirmClick()
                },
                isEnabled = isConfirmEnabled
            )
        }
    }
}

@Composable
fun KeyboardButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = Color(0xFF5CAFFF),
    isEnabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = isEnabled, onClick = onClick)
            .background(if (isEnabled) backgroundColor else Color.Gray.copy(alpha = 0.5f))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isEnabled) Color.White else Color.LightGray
        )
    }
}