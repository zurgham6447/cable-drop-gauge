package com.cabledrop.gauge.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabledrop.gauge.core.theme.AppTheme
import com.cabledrop.gauge.core.theme.numericStyle

@Composable
fun WireRow(
    gaugeLabel: String,
    areaMm2: Double,
    copper: Boolean,
    dropLabel: String,
    percentLabel: String,
    verdictLabel: String,
    bestLabel: String,
    pass: Boolean,
    recommended: Boolean,
    dimmed: Boolean,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    val shape = RoundedCornerShape(10.dp)
    val borderColor by animateColorAsState(
        targetValue = when {
            recommended -> palette.accent
            pass -> palette.border
            else -> palette.fail.copy(alpha = 0.45f)
        },
        animationSpec = tween(180),
        label = "rowBorder"
    )
    val fillColor by animateColorAsState(
        targetValue = if (recommended) {
            palette.accent.copy(alpha = 0.10f)
        } else {
            palette.surfaceAlt.copy(alpha = 0.45f)
        },
        animationSpec = tween(180),
        label = "rowFill"
    )
    val percentColor by animateColorAsState(
        targetValue = if (pass) palette.pass else palette.fail,
        animationSpec = tween(180),
        label = "rowPercent"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .alpha(if (dimmed) 0.4f else 1f)
            .clip(shape)
            .background(color = fillColor, shape = shape)
            .border(
                width = if (recommended) 1.5.dp else 1.dp,
                color = borderColor,
                shape = shape
            )
            .padding(horizontal = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .padding(vertical = 10.dp)
                .background(
                    color = if (recommended) palette.accent else palette.border.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(2.dp)
                )
        )
        Text(
            text = gaugeLabel,
            style = numericStyle(15.sp),
            color = palette.textPrimary,
            modifier = Modifier.width(76.dp)
        )
        ThicknessBar(areaMm2 = areaMm2, copper = copper)
        Text(
            text = dropLabel,
            style = numericStyle(14.sp),
            color = palette.textSecondary,
            textAlign = TextAlign.End,
            modifier = Modifier.width(62.dp)
        )
        Text(
            text = percentLabel,
            style = numericStyle(14.sp),
            color = percentColor,
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(1f)
                .semantics {
                    if (recommended) {
                        liveRegion = LiveRegionMode.Polite
                    }
                }
        )
        if (recommended) {
            Text(
                text = bestLabel,
                style = MaterialTheme.typography.labelSmall,
                color = palette.accent
            )
        }
        VerdictPill(pass = pass, label = verdictLabel)
    }
}
