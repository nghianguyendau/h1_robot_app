package com.phenikaa.h1_robot_app.presentation.features.elevator

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phenikaa.h1_robot_app.presentation.features.navigation.NavigationViewModel
import com.phenikaa.h1_robot_app.state.RobotState
import com.phenikaa.h1_robot_app.ui.theme.H1_robot_appTheme
import kotlinx.coroutines.launch

@Composable
fun RobotElevatorScreen(
    robotElevatorViewModel: ElevatorViewModel = hiltViewModel(),
    elevatorGoHomeViewModel: ElevatorGoHomeViewModel = hiltViewModel(),
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val points by elevatorGoHomeViewModel.points.collectAsState()
    val robotState by robotElevatorViewModel.robotState.collectAsState()
    val selectedDoors by robotElevatorViewModel.selectedDoors.collectAsState()
    val selectedPoint by robotElevatorViewModel.selectedPoint.collectAsState()

    // Truyền NavigationViewModel trực tiếp vào RobotElevatorViewModel
    LaunchedEffect(Unit) {
        elevatorGoHomeViewModel.navigationViewModel = navigationViewModel
        navigationViewModel.getCurrentPosition()
        elevatorGoHomeViewModel.loadPoints()
    }

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
                    Text("Chọn cửa mở", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))

                    SelectableBox(
                        text = "Cửa 1",
                        isSelected = selectedDoors.first,
                        onClick = {
                            robotElevatorViewModel.selectDoor(!selectedDoors.first, selectedDoors.second)
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    SelectableBox(
                        text = "Cửa 2",
                        isSelected = selectedDoors.second,
                        onClick = {
                            robotElevatorViewModel.selectDoor(selectedDoors.first, !selectedDoors.second)
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFE4E4E4), shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Chọn điểm đến", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Hiển thị danh sách điểm từ API
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxWidth().height(350.dp)
                    ) {
                        items(points) { point ->
                            Button(
                                onClick = { elevatorGoHomeViewModel.selectPoint(point.name ?: "Unknown", point.id) },
                                modifier = Modifier.padding(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedPoint == point.name) Color.Gray else Color.White
                                )
                            ) {
                                Text(point.name ?: "Không có", color = Color.Black)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Button(
                            onClick = {
                                Log.e("aaaa", "eeee")
                                val destinationId = elevatorGoHomeViewModel.getSelectedPointId()
                                if (destinationId != null) {
                                    elevatorGoHomeViewModel.requestRoute(destinationId)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Start", color = Color.White)
                        }
                        if (robotState == RobotState.IDLE) {
                            Button(
                                onClick = { robotElevatorViewModel.openSelectedDoors() },
//                            modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                            ) {
                                Text("Lấy hàng", color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { robotElevatorViewModel.closeDoorsAndMoveUp() },
//                            modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                            ) {
                                Text("Xác nhận lấy hàng", color = Color.White)
                            }
                        }
                    }
                }
            }

        }
    }
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
        border = BorderStroke(2.dp, if(isSelected) Color.Black else Color.White),
        colors = CardDefaults.cardColors(containerColor = Color.White)
//        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

//@Preview
//@Composable
//fun PreviewElevatorScreen(){
//    H1_robot_appTheme {
//        RobotElevatorScreen()
//    }
//}

