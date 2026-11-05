package com.cabledrop.gauge.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cabledrop.gauge.R
import com.cabledrop.gauge.core.theme.AppTheme

@Composable
fun HudGrid(alpha: Float, modifier: Modifier = Modifier) {
    val color = AppTheme.colors.gridLine
    Canvas(modifier = modifier.fillMaxSize()) {
        val step = 28.dp.toPx()
        if (step <= 0f) return@Canvas
        var x = step
        while (x < size.width) {
            drawLine(color, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f, alpha = alpha)
            x += step
        }
        var y = step
        while (y < size.height) {
            drawLine(color, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f, alpha = alpha)
            y += step
        }
    }
}

@Composable
fun CornerBrackets(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.primary,
    alpha: Float = 0.45f,
    inset: Dp = 20.dp,
    arm: Dp = 28.dp,
    stroke: Dp = 1.5.dp
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val i = inset.toPx()
        val a = arm.toPx()
        val w = stroke.toPx()
        val right = size.width - i
        val bottom = size.height - i
        if (right <= i || bottom <= i) return@Canvas
        val segments = listOf(
            Offset(i, i) to Offset(i + a, i),
            Offset(i, i) to Offset(i, i + a),
            Offset(right - a, i) to Offset(right, i),
            Offset(right, i) to Offset(right, i + a),
            Offset(i, bottom - a) to Offset(i, bottom),
            Offset(i, bottom) to Offset(i + a, bottom),
            Offset(right - a, bottom) to Offset(right, bottom),
            Offset(right, bottom - a) to Offset(right, bottom)
        )
        segments.forEach { segment ->
            drawLine(
                color = color,
                start = segment.first,
                end = segment.second,
                strokeWidth = w,
                cap = StrokeCap.Round,
                alpha = alpha
            )
        }
    }
}

@Composable
fun HudBackground(
    overlay: List<Color>,
    modifier: Modifier = Modifier,
    gridAlpha: Float = 0.25f,
    bracketAlpha: Float = 0.45f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(AppTheme.colors.bgBase)) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(overlay)))
        HudGrid(alpha = gridAlpha)
        CornerBrackets(alpha = bracketAlpha)
        content()
    }
}
