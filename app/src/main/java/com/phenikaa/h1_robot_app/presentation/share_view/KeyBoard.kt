package com.phenikaa.h1_robot_app.presentation.share_view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phenikaa.h1_robot_app.R
import com.phenikaa.h1_robot_app.presentation.theme.AppColor

@Composable
fun KeyBoard(
    onTapRemove: () -> Unit,
    onTapKeyNumber: (String) -> Unit
) {
    val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "x", "0")

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize().padding(horizontal = 200.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(numbers.size) { index ->
            KeyboardButton(
                text = numbers[index],
                onTapKeyNumber = onTapKeyNumber,
                onTapRemove = onTapRemove,
                isRemove = numbers[index] == "x"
            )
        }
    }
}

@Composable
fun KeyboardButton(
    text: String,
    onTapKeyNumber: (String) -> Unit,
    onTapRemove: () -> Unit,
    isRemove: Boolean
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .shadow(3.dp,RoundedCornerShape(12.dp),clip = true,AppColor.neutral100, spotColor = AppColor.neutral100)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = true, onClick = {
                if (isRemove) {
                    onTapRemove()
                } else {
                    onTapKeyNumber(text)
                }
            })
            .background(if (isRemove) AppColor.red400 else AppColor.neutral200)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isRemove)
            Icon(
                painter = painterResource(id = R.drawable.ic_tag_cross),
                contentDescription = "",
                modifier = Modifier.size(40.dp),
                tint = AppColor.genericWhite
            )
        else
            Text(
                text = text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AppColor.blackColor
            )
    }
}