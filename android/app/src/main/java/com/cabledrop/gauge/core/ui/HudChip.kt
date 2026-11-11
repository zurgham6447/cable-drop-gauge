package com.cabledrop.gauge.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cabledrop.gauge.R
import com.cabledrop.gauge.core.theme.AppTheme

@Composable
fun HudChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color? = null,
    height: Dp = 56.dp,
    icon: Painter? = null,
    iconDescription: String? = null
) {
    val palette = AppTheme.colors
    val tone = accent ?: palette.primary
    val shape = RoundedCornerShape(10.dp)
    val borderColor by animateColorAsState(
        targetValue = if (selected) tone else palette.border,
        animationSpec = tween(180),
        label = "chipBorder"
    )
    val fillColor by animateColorAsState(
        targetValue = if (selected) tone.copy(alpha = 0.15f) else palette.surfaceAlt.copy(alpha = 0.6f),
        animationSpec = tween(180),
        label = "chipFill"
    )
    val selectedLabel = stringResource(R.string.state_selected)
    val unselectedLabel = stringResource(R.string.state_unselected)
    Box(
        modifier = modifier
            .height(height)
            .defaultMinSize(minWidth = 56.dp)
            .clip(shape)
            .background(color = fillColor, shape = shape)
            .border(width = if (selected) 1.5.dp else 1.dp, color = borderColor, shape = shape)
            .clickable(onClick = onClick)
            .semantics { stateDescription = if (selected) selectedLabel else unselectedLabel }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Image(
                    painter = icon,
                    contentDescription = iconDescription,
                    modifier = Modifier.size(22.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) tone else palette.textSecondary
            )
        }
    }
}
