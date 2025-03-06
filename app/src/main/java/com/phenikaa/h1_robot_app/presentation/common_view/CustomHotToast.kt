package com.phenikaa.h1_robot_app.presentation.common_view

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class ToastHostState {
    private val _toasts = mutableStateListOf<Toast>()
    val toasts: List<Toast> = _toasts

    fun showToast(message: String, type: ToastType = ToastType.DEFAULT) {
        _toasts.add(Toast(message, type))
    }

    fun removeToast(toast: Toast) {
        _toasts.remove(toast)
    }

    data class Toast(
        val message: String,
        val type: ToastType,
        val id: Long = System.currentTimeMillis()
    )
}

enum class ToastType {
    SUCCESS, ERROR, LOADING, DEFAULT
}

@Composable
fun rememberToastHostState(): ToastHostState {
    return remember { ToastHostState() }
}

@Composable
fun ToastHost(
    hostState: ToastHostState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            hostState.toasts.forEach { toast ->
                key(toast.id) {
                    Toast(
                        message = toast.message,
                        onDismiss = { hostState.removeToast(toast) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Toast(
    message: String,
    onDismiss: () -> Unit,
    type: ToastType = ToastType.DEFAULT
) {
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        isVisible = false
        delay(300)
        onDismiss()
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
    ) {
        Surface(
            modifier = Modifier
                .padding(8.dp),
//                .shadow(
//                    elevation = 8.dp,
//                    shape = RoundedCornerShape(8.dp),
//                    spotColor = Color.Black.copy(alpha = 0.15f)
//                ),
            shape = RoundedCornerShape(8.dp),
            color = when (type) {
                ToastType.SUCCESS -> Color(0xFF1E824C).copy(alpha = 0.95f)
                ToastType.ERROR -> Color(0xFFDC2626).copy(alpha = 0.95f)
                ToastType.LOADING -> MaterialTheme.colorScheme.surface
                ToastType.DEFAULT -> Color(0xFF1F2937).copy(alpha = 0.95f)
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .defaultMinSize(minHeight = 40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (type) {
                    ToastType.SUCCESS -> Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    ToastType.ERROR -> Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Error",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    ToastType.LOADING -> CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                    ToastType.DEFAULT -> {}
                }

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = when (type) {
                        ToastType.LOADING -> MaterialTheme.colorScheme.onSurface
                        else -> Color.White
                    }
                )
            }
        }
    }
}