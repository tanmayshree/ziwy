package com.anonymous.ziwy.GenericComponents

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DashedDivider(
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    thickness: Dp = 1.dp,
    dashWidth: Dp = 8.dp,
    gapWidth: Dp = 4.dp
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness)
    ) {
        val pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(dashWidth.toPx(), gapWidth.toPx()), 0f
        )
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = thickness.toPx(),
            pathEffect = pathEffect
        )
    }
}