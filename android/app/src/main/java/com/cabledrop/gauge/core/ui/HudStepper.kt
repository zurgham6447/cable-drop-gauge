package com.cabledrop.gauge.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabledrop.gauge.R
import com.cabledrop.gauge.core.theme.AppTheme
import com.cabledrop.gauge.core.theme.numericStyle

@Composable
fun HudStepper(
    label: String,
    value: String,
    unit: String,
    minusDescription: String,
    plusDescription: String,
    onStepStart: (Int) -> Unit,
    onStepStop: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trailing: @Composable (() -> Unit)? = null
) {
    val palette = AppTheme.colors
    GlassCard(
        modifier = modifier,
        corner = 14.dp,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionHeading(text = label)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StepperKey(
                    label = stringResource(R.string.step_minus),
                    description = minusDescription,
                    enabled = enabled,
                    onStart = { onStepStart(-1) },
                    onStop = onStepStop
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = value,
                        style = numericStyle(24.sp),
                        color = palette.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.labelSmall,
                        color = palette.textTertiary
                    )
                }
                StepperKey(
                    label = stringResource(R.string.step_plus),
                    description = plusDescription,
                    enabled = enabled,
                    onStart = { onStepStart(1) },
                    onStop = onStepStop
                )
            }
            if (trailing != null) {
                trailing()
            }
        }
    }
}

@Composable
private fun StepperKey(
    label: String,
    description: String,
    enabled: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    val palette = AppTheme.colors
    val shape = RoundedCornerShape(10.dp)
    Box(
        modifier = Modifier
            .size(48.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clip(shape)
            .background(color = palette.surfaceAlt, shape = shape)
            .border(width = 1.dp, color = palette.border, shape = shape)
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                detectTapGestures(
                    onPress = {
                        onStart()
                        tryAwaitRelease()
                        onStop()
                    }
                )
            }
            .semantics {
                contentDescription = description
                role = Role.Button
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = numericStyle(22.sp),
            color = if (enabled) palette.primary else palette.textTertiary
        )
    }
}
