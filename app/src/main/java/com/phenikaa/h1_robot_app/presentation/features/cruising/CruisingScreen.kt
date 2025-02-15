package com.phenikaa.h1_robot_app.presentation.features.cruising

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phenikaa.h1_robot_app.data.model.Point
import com.phenikaa.h1_robot_app.presentation.shared.PhenikaaMecViewModel


data class MapItem(
    val id: String,
    val name: String
)


@Composable
fun CruisingScreen(viewModel: PhenikaaMecViewModel = hiltViewModel()) {
    // Mock data với nhiều điểm hơn để test
    val floors by viewModel.floors.collectAsState()
    val points by viewModel.points.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

    var selectedMap by remember { mutableStateOf<MapItem?>(null) }
    var selectedPoints by remember { mutableStateOf(setOf<Point>()) }

    LaunchedEffect(Unit) {
        viewModel.getAllFloors()
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Left panel - Map list
        Card(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight()
                .padding(end = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Danh sách bản đồ",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (floors.isEmpty()) {
                    Text("Không có dữ liệu tầng", color = MaterialTheme.colorScheme.error)
                } else {
                    LazyColumn {
                        items(floors) { floor ->
                            MapItem(
                                map = MapItem(floor.id.toString(), floor.name),
                                isSelected = selectedMap?.id == floor.id.toString(),
                                onClick = {
                                    selectedMap = MapItem(floor.id.toString(), floor.name)
                                    selectedPoints = emptySet() // Reset điểm đã chọn
                                    viewModel.getPointsByFloorId(floor.id) // Gọi API lấy danh sách điểm
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                    }
                }
            }
        }

        // Right panel - Points and controls
        Card(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // Selected map info
                selectedMap?.let {
                    Text(
                        text = "Bản đồ: ${it.name}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Points grid
                if (selectedMap != null) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        // Points selection
                        Column(
                            modifier = Modifier
                                .weight(0.7f)
                                .padding(end = 16.dp)
                        ) {
                            Text(
                                text = "Chọn điểm đến:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 150.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(points) { point ->
                                    PointItem(
                                        point = point,
                                        isSelected = point in selectedPoints,
                                        onClick = {
                                            selectedPoints = if (point in selectedPoints) {
                                                selectedPoints - point
                                            } else {
                                                selectedPoints + point
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        // Selected points list
                        if (selectedPoints.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .weight(0.3f)
                                    .fillMaxHeight()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize()
                                ) {
                                    Text(
                                        text = "Điểm đã chọn (${selectedPoints.size}):",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(selectedPoints.toList()) { point ->
                                            ElevatedFilterChip(
                                                selected = true,
                                                onClick = {
                                                    selectedPoints = selectedPoints - point
                                                },
                                                label = { point.name?.let { Text(it) } },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Start button
                    Button(
                        onClick = { /* TODO: Implement start navigation */ },
                        modifier = Modifier.align(Alignment.End),
                        enabled = selectedPoints.isNotEmpty()
                    ) {
                        Text("Bắt đầu di chuyển")
                    }
                }
            }
        }
    }
}

@Composable
private fun MapItem(
    map: MapItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = map.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun PointItem(
    point: Point,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            point.name?.let { Text(text = it) }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "X: ${point.x}, Y: ${point.y}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}