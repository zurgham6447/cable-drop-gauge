package com.cabledrop.gauge.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabledrop.gauge.R
import com.cabledrop.gauge.core.config.GameConfig
import com.cabledrop.gauge.core.theme.AppTheme
import com.cabledrop.gauge.core.theme.numericStyle
import com.cabledrop.gauge.core.ui.decimals
import com.cabledrop.gauge.domain.model.LoadPoint

@Composable
fun LoadDialog(
    initial: LoadPoint?,
    onDismiss: () -> Unit,
    onSave: (String, Double, Double) -> Unit
) {
    val palette = AppTheme.colors
    val fallbackName = stringResource(R.string.load_default_name)
    var name by rememberSaveable(initial?.id) { mutableStateOf(initial?.name ?: "") }
    var watts by rememberSaveable(initial?.id) {
        mutableStateOf(initial?.watts ?: 10.0)
    }
    var distance by rememberSaveable(initial?.id) {
        mutableStateOf(initial?.distanceMeters ?: 3.0)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = palette.surface,
        titleContentColor = palette.textPrimary,
        textContentColor = palette.textSecondary,
        title = {
            Text(
                text = stringResource(
                    if (initial == null) R.string.dialog_add_load else R.string.dialog_edit_load
                ),
                style = MaterialTheme.typography.titleSmall
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { entered -> name = entered.take(28) },
                    singleLine = true,
                    label = { Text(text = stringResource(R.string.dialog_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                DialogStepper(
                    label = stringResource(R.string.dialog_watts),
                    value = decimals(watts, 0),
                    onMinus = {
                        watts = (watts - GameConfig.LOAD_POINT_STEP_WATTS)
                            .coerceAtLeast(GameConfig.MIN_LOAD_POINT_WATTS)
                    },
                    onPlus = {
                        watts = (watts + GameConfig.LOAD_POINT_STEP_WATTS)
                            .coerceAtMost(GameConfig.MAX_LOAD_POINT_WATTS)
                    }
                )
                DialogStepper(
                    label = stringResource(R.string.dialog_distance),
                    value = decimals(distance, 1),
                    onMinus = {
                        distance = (distance - GameConfig.DIAGRAM_DISTANCE_STEP)
                            .coerceAtLeast(GameConfig.MIN_DIAGRAM_DISTANCE)
                    },
                    onPlus = {
                        distance = (distance + GameConfig.DIAGRAM_DISTANCE_STEP)
                            .coerceAtMost(GameConfig.MAX_DIAGRAM_DISTANCE)
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(name.trim().ifEmpty { fallbackName }, watts, distance)
                },
                modifier = Modifier.height(48.dp)
            ) {
                Text(text = stringResource(R.string.cta_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.height(48.dp)) {
                Text(text = stringResource(R.string.cta_cancel))
            }
        }
    )
}

@Composable
private fun DialogStepper(
    label: String,
    value: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    val palette = AppTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = palette.textTertiary,
            modifier = Modifier.width(74.dp)
        )
        OutlinedButton(
            onClick = onMinus,
            modifier = Modifier
                .width(52.dp)
                .height(48.dp)
        ) {
            Text(text = stringResource(R.string.step_minus))
        }
        Text(
            text = value,
            style = numericStyle(18.sp),
            color = palette.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        OutlinedButton(
            onClick = onPlus,
            modifier = Modifier
                .width(52.dp)
                .height(48.dp)
        ) {
            Text(text = stringResource(R.string.step_plus))
        }
    }
}
