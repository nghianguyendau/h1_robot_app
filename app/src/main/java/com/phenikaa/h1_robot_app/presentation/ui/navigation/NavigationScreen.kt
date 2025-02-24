package com.phenikaa.h1_robot_app.presentation.ui.navigation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csjbot.coshandler.listener.OnMapListener

@Composable
fun NavigationScreen(
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val navigationState by viewModel.navigationState.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val savedPosition by viewModel.savedPosition.collectAsState()
    val navigationResult by viewModel.navigationResult.collectAsState()

    val currentSpeed by viewModel.currentSpeed.collectAsState()

    val mapList by viewModel.mapList.collectAsState()
    val mapListError by viewModel.mapListError.collectAsState()
    val selectedFloors by viewModel.selectedFloors.collectAsState()
    val selectedMap by viewModel.selectedMap.collectAsState()

    val position = """{"x": -6.4490547, "y": -14.451439, "z": 0.0, "rotation": 126.7954562}"""


    val mapListener = remember {
        object : OnMapListener {
            override fun saveMap(status: Int) {
                // Xử lý khi lưu map
                Log.d("MapListener", "Save map status: $status")
            }

            override fun loadMap(status: Int) {
                // Xử lý khi load map
                when (status) {
                    0 -> Log.d("MapListener", "Map loading started")
                    100 -> Log.d("MapListener", "Map loaded successfully")
                    else -> Log.d("MapListener", "Loading progress: $status")
                }
            }
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    var pointName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getCurrentPosition()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Current Position Display
        currentPosition?.let { position ->
            Text(
                text = "Current Position:",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "X: ${position.x}, Y: ${position.y}, Rotation: ${position.rotation}°"
            )
        } ?: Text("Fetching current position...")

        // Navigation State Display
//        when (navigationState) {
//            is NavigationState.Navigating -> {
//                CircularProgressIndicator()
//                Text("Navigating...")
//            }
//            is NavigationState.Completed -> {
//                Text("Navigation completed successfully")
//            }
//            is NavigationState.Error -> {
//                Text(
//                    text = (navigationState as NavigationState.Error).message,
//                    color = MaterialTheme.colorScheme.error
//                )
//            }
//            is NavigationState.Idle -> {
//                Text("Ready to navigate")
//            }
//        }
        when (navigationResult) {
            true -> Text("Navigation completed successfully!")
            false -> Text("Navigation failed.")
            null -> Text("Ready to navigate.")
        }

        // Navigation Controls
        Row {
            Column(modifier = Modifier
                .fillMaxHeight()
                .imePadding()
                .verticalScroll(
                    rememberScrollState()
                )) {
                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DirectionButton(
                        text = "Turn Left",
                        onClick = {
                            viewModel.moveDirection(2)
                        }
                    )
                    DirectionButton(
                        text = "Turn Right",
                        onClick = {
                            viewModel.moveDirection(3)
                        }
                    )
                    DirectionButton(
                        text = "Forward",
                        onClick = {
                            viewModel.moveDirection(0)
                        }
                    )
                    DirectionButton(
                        text = "Backward",
                        onClick = {
                            viewModel.moveDirection(1)
                        }
                    )
                }

                //            Row(
                //                modifier = Modifier.fillMaxWidth(),
                //                horizontalArrangement = Arrangement.SpaceEvenly
                //            ) {
                //                DirectionButton(
                //                    text = "Move Forward",
                //                    onClick = { viewModel.moveBySerial(0x01) }
                //                )
                //                DirectionButton(
                //                    text = "Turn Left",
                //                    onClick = { viewModel.moveBySerial(0x03) }
                //                )
                //                DirectionButton(
                //                    text = "Turn Right",
                //                    onClick = { viewModel.moveBySerial(0x04) }
                //                )
                //                DirectionButton(
                //                    text = "Move Forward",
                //                    onClick = { viewModel.moveBySerial(0x02) }
                //                )
                //            }

                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DirectionButton(
                        text = "Rotate 60",
                        onClick = { viewModel.goAngle(60) }
                    )
                    DirectionButton(
                        text = "Rotate 120",
                        onClick = { viewModel.goAngle(120) }
                    )
                    DirectionButton(
                        text = "Rotate 90 (moveAngle)",
                        onClick = { viewModel.moveAngle(90) } // Quay 90 độ
                    )
                    DirectionButton(
                        text = "Rotate 270 (moveAngle)",
                        onClick = { viewModel.moveAngle(270) } // Quay 270 độ
                    )
                }

                //            Row(
                //                modifier = Modifier.fillMaxWidth(),
                //                horizontalArrangement = Arrangement.SpaceEvenly
                //            ) {
                //                DirectionButton(
                //                    text = "Rotate 90°",
                //                    onClick = { viewModel.moveAngle(90) } // Quay 90 độ
                //                )
                //                DirectionButton(
                //                    text = "Rotate 270°",
                //                    onClick = { viewModel.moveAngle(270) } // Quay 270 độ
                //                )
                //            }

                //            Row {
                //                DirectionButton(
                //                    text = "Navigate to (1.0, 2.0, 0.0)",
                //                    onClick = {
                //                        val position = RosPosition(
                //                            poseName = "Docking Station",
                //                            pos = RosPosition.PosBean(
                //                                x = 1.0f,
                //                                y = 2.0f,
                //                                z = 0.0f,
                //                                rotation = 90.0f
                //                            )
                //                        )
                //                        viewModel.navigateToPosition(position)
                //                    }
                //                )
                //            }

                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DirectionButton(
                        text = "Save Position",
                        onClick = { showDialog = true }
                    )

                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Nhập Tên Điểm") },
                            text = {
                                TextField(
                                    value = pointName,
                                    onValueChange = { pointName = it },
                                    label = { Text("Tên điểm") }
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        if (pointName.isNotEmpty()) {
                                            viewModel.saveCurrentPosition(pointName)
                                            showDialog = false
                                        }
                                    }
                                ) {
                                    Text("Xác nhận")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showDialog = false }) {
                                    Text("Hủy")
                                }
                            }
                        )
                    }
                    DirectionButton(
                        text = "Go",
                        onClick = { viewModel.navigateToSavedPosition() }
                    )

                    DirectionButton(
                        text = "Go 2",
                        onClick = { viewModel.navigateToSavedPosition2() }
                    )
                    DirectionButton(
                        text = "Go Des",
                        onClick = { viewModel.navigateToDestination(position)

                        Log.e("ddddddddddddddddd", "ddddddddddddddd")
                        }
                    )

                    DirectionButton(
                        text = "Go home",
                        onClick = {
                            viewModel.goHome()
                        }
                    )
                    DirectionButton(
                        text = "Cancel navi",
                        onClick = {
                            viewModel.cancelNavi()
                        }
                    )
                }

                Row {
                    // Hiển thị tốc độ hiện tại
//                    Text(
//                        text = currentSpeed?.let { "Current Speed: $it m/s" } ?: "Speed not available",
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                    DirectionButton(
//                        text = "Get current speed",
//                        onClick = {
//                            viewModel.fetchCurrentSpeed()
//                        }
//                    )
                    DirectionButton(
                        text = "Set speed 0.8 m/s",
                        onClick = {
                            viewModel.setSpeed(0.8f)
                        }
                    )
                    DirectionButton(
                        text = "Set speed 0.4 m/s",
                        onClick = {
                            viewModel.setSpeed(0.4f)
                        }
                    )
                }



                //            Row {
                //                Text("Map Controls", style = MaterialTheme.typography.titleMedium)
                //
                //                // Nút tải bản đồ mặc định
                //                Button(onClick = { viewModel.loadDefaultMap() }) {
                //                    Text("Load Default Map")
                //                }
                //
                //                // Nút tải bản đồ theo tên
                //                Button(onClick = { viewModel.loadMapByName("map1") }) {
                //                    Text("Load Map map1")
                //                }
                //
                ////                // Nút tải bản đồ với tọa độ cụ thể
                ////                Button(onClick = {
                ////                    viewModel.loadMapToPosition("12A", 1.0f, 2.0f, 90.0f)
                ////                }) {
                ////                    Text("Load Map to Position")
                ////                }
                //                // Nút tải bản đồ theo tên
                //                Button(onClick = { viewModel.loadMapByName("map2") }) {
                //                    Text("Load Map map2")
                //                }
                //                // Nút tải bản đồ theo tên
                //                Button(onClick = { viewModel.loadMapByName("12A") }) {
                //                    Text("Load Map 12A")
                //                }
                //            }

                Text(
                    text = "Vị Trí Đã Lưu:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (savedPosition != null) {
                    Text(
                        text = "Tên điểm: ${savedPosition?.poseName}\n" +
                                "X: ${savedPosition?.pos?.x}, Y: ${savedPosition?.pos?.y}, Z: ${savedPosition?.pos?.z}\n" +
                                "Rotation: ${savedPosition?.pos?.rotation}",
                        fontSize = 16.sp
                    )
                } else {
                    Text("Chưa có vị trí nào được lưu", fontSize = 16.sp, color = Color.Gray)
                }


            }
            Column {
                Text("Map List", style = MaterialTheme.typography.titleMedium)

                // Nút lấy danh sách bản đồ
                Button(onClick = { viewModel.fetchMapList() }) {
                    Text("Fetch Map List")
                }

                // Hiển thị lỗi nếu có
                mapListError?.let {
                    Text(
                        text = "Error: $it",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                // Hiển thị danh sách bản đồ
                LazyColumn {
                    items(mapList) { mapName ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { viewModel.selectMap(mapName) },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = mapName)
                            if (mapName == selectedFloors) {
                                Text(
                                    text = "Selected",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                // Nút Load Map
                if (!selectedFloors.isNullOrEmpty()) {
                    Button(onClick = { viewModel.loadSelectedMap() }) {
                        Text("Load Map: $selectedMap")
                    }
                }
            }
        }
    }
}

@Composable
private fun DirectionButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text)
    }
}