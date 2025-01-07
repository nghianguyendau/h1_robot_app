package com.phenikaa.h1_robot_app.presentation.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.phenikaa.h1_robot_app.ui.components.LottieAnimation
import com.phenikaa.h1_robot_app.R

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF6FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Header section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LottieAnimation()
                Column {
                    Text(
                        text = "Hello,",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium,
                        fontSize = 48.sp,
                        color = Color(0xFF5CAFFF)
                    )
                    Text(
                        text = "I'm Robot H1",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium,
                        fontSize = 48.sp,
                        color = Color(0xFF333333)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cards section with new layout
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left large card (Delivery)
                GradientBox(
                    primaryColor = 0xFF0078D7,
                    secondaryColor = 0xFF81D4FA,
                    imageResId = R.drawable.box,
                    title = "Giao hàng",
                    subTitle = "Delivery",
                    modifier = Modifier.weight(1f),
                    heightType = BoxHeightType.FULL,
                    layoutType = BoxLayoutType.VERTICAL,
                    onClick = {
                        navController.navigate("phone_call")
                    }
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GradientBox(
                        primaryColor = 0xFFffa000,
                        secondaryColor = 0xFFffe082,
                        imageResId = R.drawable.map,
                        title = "Bản đồ",
                        subTitle = "Map",
                        heightType = BoxHeightType.HALF,
                        layoutType = BoxLayoutType.HORIZONTAL,
                        onClick = {
                            navController.navigate("door_screen")
                        }
                    )

                    GradientBox(
                        primaryColor = 0xFF00c853,
                        secondaryColor = 0xFFb9f6ca,
                        imageResId = R.drawable.comments,
                        title = "Nhắn tin",
                        subTitle = "Message",
                        heightType = BoxHeightType.HALF,
                        layoutType = BoxLayoutType.HORIZONTAL,
                        onClick = {

                        }
                    )
                }
            }
        }
    }
}

enum class BoxHeightType {
    FULL, HALF
}

enum class BoxLayoutType {
    VERTICAL, HORIZONTAL
}

@Composable
fun GradientBox(
    primaryColor: Long,
    secondaryColor: Long,
    imageResId: Int,
    title: String,
    subTitle: String,
    modifier: Modifier = Modifier,
    heightType: BoxHeightType = BoxHeightType.HALF,
    layoutType: BoxLayoutType = BoxLayoutType.VERTICAL,
    onClick: () -> Unit
) {
    val boxHeight = when (heightType) {
        BoxHeightType.FULL -> 400.dp
        BoxHeightType.HALF -> 192.dp
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(boxHeight)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(primaryColor),
                        Color(secondaryColor)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                onClick()
            }
            .padding(16.dp)
          ,
        contentAlignment = Alignment.Center
    ) {
        when (layoutType) {
            BoxLayoutType.VERTICAL -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(imageResId),
                        contentDescription = null,
                        modifier = Modifier.size(
                            when (heightType) {
                                BoxHeightType.FULL -> 160.dp
                                BoxHeightType.HALF -> 80.dp
                            }
                        ),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = when (heightType) {
                                BoxHeightType.FULL -> 32.sp
                                BoxHeightType.HALF -> 24.sp
                            },
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = when (heightType) {
                                BoxHeightType.FULL -> 28.sp
                                BoxHeightType.HALF -> 20.sp
                            },
                            color = Color.White
                        )
                    )
                }
            }
            BoxLayoutType.HORIZONTAL -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(imageResId),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
                    )

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = subTitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}