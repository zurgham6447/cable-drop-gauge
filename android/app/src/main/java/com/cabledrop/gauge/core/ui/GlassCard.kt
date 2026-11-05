package com.cabledrop.gauge.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cabledrop.gauge.core.theme.AppTheme

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = AppTheme.colors.border,
    borderWidth: Dp = 1.dp,
    corner: Dp = 16.dp,
    fill: Brush? = null,
    showBrackets: Boolean = false,
    bracketColor: Color = AppTheme.colors.primary,
    contentPadding: PaddingValues = PaddingValues(14.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(corner)
    val background = fill ?: SolidColor(AppTheme.colors.surfaceAlt.copy(alpha = 0.72f))
    Box(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = AppTheme.colors.primary.copy(alpha = 0.25f),
                spotColor = AppTheme.colors.primary.copy(alpha = 0.25f)
            )
            .clip(shape)
            .background(brush = background, shape = shape)
            .border(width = borderWidth, color = borderColor, shape = shape)
    ) {
        if (showBrackets) {
            CornerBrackets(
                modifier = Modifier.fillMaxSize(),
                color = bracketColor,
                alpha = 0.5f,
                inset = 7.dp,
                arm = 14.dp,
                stroke = 1.2.dp
            )
        }
        Box(modifier = Modifier.padding(contentPadding), content = content)
    }
}
