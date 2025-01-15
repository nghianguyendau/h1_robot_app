package com.phenikaa.h1_robot_app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phenikaa.h1_robot_app.R
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.delay

@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    serialNumber: String = "000674500123301",
    batteryPercentage: Int = 100,
    showBack: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    Column(modifier = modifier) {
        // Original Top App Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            color = Color(0xFFEFF6FF)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo
                Image(
                    painter = painterResource(R.drawable.logo_pnkx),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(160.dp) // Giảm kích thước logo
                        .align(Alignment.CenterVertically) // Căn giữa logo trong hàng
                )

//                Spacer(modifier = Modifier.weight(1f)) // Đẩy các thành phần còn lại sang bên phải

                var currentTime by remember { mutableStateOf(Calendar.getInstance().time) }
                val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(1000)
                        currentTime = Calendar.getInstance().time
                    }
                }

                // Hiển thị thời gian
                Text(
                    text = timeFormat.format(currentTime),
                    color = Color.Black,
                    fontSize = 20.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Serial number
                    Text(
                        text = serialNumber,
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    // WiFi Icon
                    Icon(
                        painter = painterResource(R.drawable.rounded_wifi_24),
                        contentDescription = "WiFi Status",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )

                    // Battery status
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.rounded_battery_full_alt_24),
                            contentDescription = "Battery Status",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "$batteryPercentage%",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Back Navigation Bar
        if (showBack) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                color = Color(0xFFEFF6FF)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}
