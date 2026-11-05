package com.cabledrop.gauge.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.cabledrop.gauge.core.theme.AppTheme
import kotlin.math.sqrt

@Composable
fun ThicknessBar(
    areaMm2: Double,
    copper: Boolean,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    val stops = if (copper) {
        listOf(palette.copperStart, palette.copperEnd)
    } else {
        listOf(palette.aluminiumStart, palette.aluminiumEnd)
    }
    val minRoot = sqrt(0.5)
    val maxRoot = sqrt(35.0)
    val ratio = ((sqrt(areaMm2.coerceAtLeast(0.01)) - minRoot) / (maxRoot - minRoot))
        .coerceIn(0.0, 1.0)
    val barHeight = (3.0 + ratio * 15.0).toFloat()
    Canvas(
        modifier = modifier
            .width(54.dp)
            .height(18.dp)
    ) {
        val thickness = barHeight.dp.toPx()
        val top = (size.height - thickness) / 2f
        drawRoundRect(
            brush = Brush.horizontalGradient(stops),
            topLeft = Offset(0f, top),
            size = Size(size.width, thickness),
            cornerRadius = CornerRadius(thickness / 2f, thickness / 2f)
        )
    }
}
