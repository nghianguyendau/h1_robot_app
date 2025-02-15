package com.phenikaa.h1_robot_app.presentation.features.saveposition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.phenikaa.h1_robot_app.data.model.Point
import com.phenikaa.h1_robot_app.data.model.RosPosition

@Composable
fun SavePositionScreen(viewModel: SavePositionViewModel = hiltViewModel()) {
    val points by viewModel.points.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPoints()
        viewModel.getCurrentPosition()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

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
       Row(
            horizontalArrangement = Arrangement.SpaceBetween,
       ) {
           Text("Danh sách Điểm đã lưu (Trang $currentPage / $totalPages)", fontSize = 22.sp, fontWeight = FontWeight.Bold)
           Spacer(modifier = Modifier.weight(1f))
           Button(
               onClick = { showDialog = true },
           ) {
               Text("Thêm Điểm Mới")
           }

           if (showDialog) {
               AddPointDialog(viewModel) { showDialog = false }
           }
       }

        TableHeader()

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(points) { point ->
                TableRow(point, viewModel)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PaginationControls(viewModel, currentPage, totalPages)
        }
    }
}

@Composable
fun PointItem(point: Point, viewModel: SavePositionViewModel = hiltViewModel()) {
    var showEditDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = "ID: ${point.id}", fontWeight = FontWeight.Bold)
            Text(text = "Tên: ${point.name ?: "UnKnown"}")
            Text(text = "X: ${point.x}, Y: ${point.y}, Z: ${point.z ?: "N/A"}")
            Text(text = "Rotation: ${point.rotation}")
            Text(text = "${point.floor?.name ?: "Không xác định"}")
            Text(text = "Type: ${point.getTypeLabel()}")


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { showEditDialog = true }) {
                    Text("Sửa")
                }
                Button(onClick = { viewModel.deletePoint(point.id) }) {
                    Text("Xóa")
                }
            }
            if (showEditDialog) {
                EditPointDialog(point, viewModel) { showEditDialog = false }
            }
        }
    }
}

@Composable
fun TableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TableCell("ID", weight = 0.2f, isHeader = true)
        TableCell("Tên", weight = 0.4f, isHeader = true)
        TableCell("Tầng", weight = 0.2f, isHeader = true)
        TableCell("Hành động", weight = 0.3f, isHeader = true)
    }
}

@Composable
fun TableRow(point: Point, viewModel: SavePositionViewModel) {
    var showEditDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TableCell(point.id.toString(), weight = 0.2f)
        TableCell(point.name ?: "Không có", weight = 0.4f)
        TableCell(point.floor?.name ?: "N/A", weight = 0.2f)

        Row(
//            modifier = Modifier.weight(0.3f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { showEditDialog = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Sửa", tint = Color.Blue)
            }
            IconButton(onClick = { viewModel.deletePoint(point.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
            }
        }
    }

    if (showEditDialog) {
        EditPointDialog(point, viewModel) { showEditDialog = false }
    }
}
@Composable
fun TableCell(text: String, weight: Float, isHeader: Boolean = false) {
    Text(
        text = text,
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .padding(8.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun AddPointDialog(viewModel: SavePositionViewModel, onDismiss: () -> Unit) {
    var floorCode by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("0") }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Thêm Điểm Mới", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                TextField(
                    value = floorCode,
                    onValueChange = { floorCode = it },
                    label = { Text("Floor Code") }
                )
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên điểm") }
                )
                TextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Loại điểm") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Button(onClick = {
                        val typeInt = type.toIntOrNull() ?: 0
                        viewModel.savePoint(floorCode, name, typeInt)
                        onDismiss()
                    }) {
                        Text("Thêm")
                    }
                }
            }
        }
    }
}

@Composable
fun EditPointDialog(point: Point, viewModel: SavePositionViewModel, onDismiss: () -> Unit) {
    var floorCode by remember { mutableStateOf(point.floor?.code ?: "") }
    var name by remember { mutableStateOf(point.name ?: "") }
    var type by remember { mutableStateOf(point.type.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .imePadding()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Chỉnh Sửa Điểm", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                TextField(value = floorCode, onValueChange = { floorCode = it }, label = { Text("Floor Code") })
                TextField(value = name, onValueChange = { name = it }, label = { Text("Tên điểm") })
                TextField(value = type, onValueChange = { type = it }, label = { Text("Loại điểm (0-2)") })

//                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Button(
                        onClick = {
                            val typeInt = type.toIntOrNull() ?: 0
                            viewModel.updatePoint(
                                point.id, floorCode, name,
                                RosPosition(name, RosPosition.PosBean(point.x, point.y, point.z ?: 0.0f, point.rotation)), typeInt
                            )
                            onDismiss()
                        }
                    ) {
                        Text("Lưu")
                    }
                }
            }
        }
    }
}

@Composable
fun PaginationControls(viewModel: SavePositionViewModel, currentPage: Int, totalPages: Int) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(totalPages) { page ->
            Button(
                onClick = { viewModel.loadPoints(page + 1) },
                colors = if (currentPage == page + 1) ButtonDefaults.buttonColors(containerColor = Color.Blue) else ButtonDefaults.buttonColors()
            ) {
                Text(text = "${page + 1}", color = if (currentPage == page + 1) Color.White else Color.Black)
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}







