package com.cabledrop.gauge.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabledrop.gauge.core.theme.AppTheme
import com.cabledrop.gauge.core.theme.numericStyle

@Composable
fun StatCard(
    value: String,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    iconDescription: String? = null,
    announce: Boolean = false
) {
    GlassCard(
        modifier = modifier,
        borderColor = accent.copy(alpha = 0.45f),
        corner = 14.dp,
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color = accent, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (icon != null) {
                    Image(
                        painter = icon,
                        contentDescription = iconDescription,
                        modifier = Modifier.size(26.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Text(
                text = value,
                style = numericStyle(22.sp),
                color = accent,
                modifier = if (announce) {
                    Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                } else {
                    Modifier
                }
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = AppTheme.colors.textTertiary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
