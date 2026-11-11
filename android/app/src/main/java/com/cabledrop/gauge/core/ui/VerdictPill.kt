package com.cabledrop.gauge.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cabledrop.gauge.core.theme.AppTheme

@Composable
fun VerdictPill(
    pass: Boolean,
    label: String,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    val tone by animateColorAsState(
        targetValue = if (pass) palette.pass else palette.fail,
        animationSpec = tween(180),
        label = "verdictTone"
    )
    val fill by animateColorAsState(
        targetValue = if (pass) palette.pass.copy(alpha = 0.15f) else palette.fail.copy(alpha = 0.15f),
        animationSpec = tween(180),
        label = "verdictFill"
    )
    Box(
        modifier = modifier
            .height(24.dp)
            .defaultMinSize(minWidth = 54.dp)
            .background(color = fill, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = tone
        )
    }
}
