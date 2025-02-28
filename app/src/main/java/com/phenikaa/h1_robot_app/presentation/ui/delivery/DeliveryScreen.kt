package com.phenikaa.h1_robot_app.presentation.ui.elevator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.phenikaa.h1_robot_app.presentation.common_view.CommonScaffold
import com.phenikaa.h1_robot_app.presentation.navigation.Screen
import com.phenikaa.h1_robot_app.presentation.ui.navigation.NavigationViewModel

@Composable
fun DeliveryScreen(
    appNavController: NavHostController,
    robotDeliveryViewModel: DeliveryViewModel = hiltViewModel(),
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val points by robotDeliveryViewModel.points.collectAsState()
    val robotState by robotDeliveryViewModel.robotState.collectAsState()
    val selectedDoors by robotDeliveryViewModel.selectedDoors.collectAsState()
    val selectedPoint by robotDeliveryViewModel.selectedPoints.collectAsState()
    val currentAction by robotDeliveryViewModel.currentAction.collectAsState()
    val isDeliveryConfirmed by robotDeliveryViewModel.isDeliveryConfirmed.collectAsState()
    val allStagesCompleted by robotDeliveryViewModel.allStagesCompleted.collectAsState()
    var phoneNumber by remember { mutableStateOf("") }


    // Truyền NavigationViewModel trực tiếp vào RobotElevatorViewModel
    LaunchedEffect(Unit) {
        robotDeliveryViewModel.navigationViewModel = navigationViewModel
        navigationViewModel.getCurrentPosition()
        robotDeliveryViewModel.loadPoints()
    }

    CommonScaffold(content = { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEFF6FF))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFD8D8D8),
                                    Color(0xFF81D4FA)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Chọn cửa mở",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        SelectableBox(
                            text = "Cửa 1",
                            isSelected = selectedDoors[0],
                            onClick = {
                                robotDeliveryViewModel.selectDoor(
                                    !selectedDoors[0],
                                    selectedDoors[1]
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SelectableBox(
                            text = "Cửa 2",
                            isSelected = selectedDoors[1],
                            onClick = {
                                robotDeliveryViewModel.selectDoor(
                                    selectedDoors[0],
                                    !selectedDoors[1]
                                )
                            }
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFFDFCF1),
                                    Color(0xFFEBEAEA)
                                )
                            ), shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // 🔹 Nhập số điện thoại
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Nhập số điện thoại",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_call_decline),
                                        contentDescription = "Số điện thoại"
                                    )
                                },
                                label = { Text("Nhập số điện thoại") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Chọn điểm đến",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Hiển thị danh sách điểm từ API
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp)
                            ) {
                                items(points) { point ->
                                    Button(
                                        onClick = {
                                            robotDeliveryViewModel.selectPoint(
                                                point.name ?: "Unknown", point.id
                                            )
                                        },
                                        modifier = Modifier.padding(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedPoint.any { it.first == point.name }) Color.Gray else Color.White
                                        )
                                    ) {
                                        Text(point.name ?: "Không có", color = Color.Black)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.End // Canh lề phải
                        ) {
                            if (currentAction == "DeliveryNotification" && !isDeliveryConfirmed) {
                                Button(
                                    onClick = {
//                                robotElevatorViewModel.openSelectedDoors()
                                        robotDeliveryViewModel.confirmDelivery()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) {
                                    Text("Lấy hàng", color = Color.White)
                                }
                            } else if (currentAction == "DeliveryNotification" && isDeliveryConfirmed) {
                                Button(
                                    onClick = { robotDeliveryViewModel.closeDoorsAndMoveUp() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                                ) {
                                    Text("Xác nhận lấy hàng", color = Color.White)
                                }
                            } else {
                                Button(
                                    onClick = {
                                        val destinationIds = selectedPoint.map { it.second }
                                        if (destinationIds.isNotEmpty()) {
                                            robotDeliveryViewModel.requestRoute(destinationIds)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                                ) {
                                    Text("Giao hàng", color = Color.White)
                                }
                            }
                        }
                    }
                }

            }
        }
    }, navController = appNavController,
        onTapBackButton = { appNavController.navigate(Screen.Home.route) }
    )
}

@Composable
fun SelectableBox(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
//            .background(
//                color = Color.White,
//                shape = RoundedCornerShape(10.dp)
//            )
            .padding(8.dp)
            .clickable { onClick() },
        border = BorderStroke(2.dp, if (isSelected) Color.Black else Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.White)
//        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

//@Preview
//@Composable
//fun PreviewElevatorScreen(){
//    H1_robot_appTheme {
//        RobotElevatorScreen()
//    }
//}

