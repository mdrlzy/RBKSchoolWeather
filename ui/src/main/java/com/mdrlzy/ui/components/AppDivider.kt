package com.mdrlzy.ui.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppHorDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.White.copy(alpha = 0.12f),
    )
}