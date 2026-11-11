package com.cabledrop.gauge.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cabledrop.gauge.R
import com.cabledrop.gauge.core.config.GameConfig
import com.cabledrop.gauge.core.di.ServiceLocator
import com.cabledrop.gauge.core.theme.AppTheme
import com.cabledrop.gauge.core.theme.numericStyle
import com.cabledrop.gauge.core.ui.GlassCard
import com.cabledrop.gauge.core.ui.HomeTile
import com.cabledrop.gauge.core.ui.HudBackground
import com.cabledrop.gauge.core.ui.HudChip
import com.cabledrop.gauge.core.ui.SecondaryButton
import com.cabledrop.gauge.core.ui.SectionHeading
import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.SizeSystem
import com.cabledrop.gauge.presentation.dialog.ConfirmDialog
import kotlin.math.roundToInt

@Composable
fun WireSettingsPanel(
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    val settingsViewModel: WireSettingsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    if (state.confirmVisible) {
        ConfirmDialog(
            title = stringResource(R.string.settings_clear_title),
            message = stringResource(R.string.settings_clear_message),
            onConfirm = settingsViewModel::confirmClear,
            onDismiss = settingsViewModel::dismissClear
        )
    }

    WireSettingsContent(
        state = state,
        onBack = onBack,
        onHome = onHome,
        onSizeSystem = settingsViewModel::setSizeSystem,
        onLossLimit = settingsViewModel::setLossLimit,
        onMaterial = settingsViewModel::setMaterial,
        onVoltage = settingsViewModel::setSystemVoltage,
        onLightMode = settingsViewModel::setLightMode,
        onClear = settingsViewModel::askClear
    )
}

@Composable
private fun WireSettingsContent(
    state: WireSettingsUiState,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onSizeSystem: (SizeSystem) -> Unit,
    onLossLimit: (Int) -> Unit,
    onMaterial: (ConductorMaterial) -> Unit,
    onVoltage: (Int) -> Unit,
    onLightMode: (Boolean) -> Unit,
    onClear: () -> Unit
) {
    val palette = AppTheme.colors

    HudBackground(
        overlay = listOf(
            palette.bgDeep.copy(alpha = 0.92f),
            palette.surfaceAlt.copy(alpha = 0.62f),
            palette.bgBase.copy(alpha = 0.94f)
        ),
        gridAlpha = 0.18f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                HomeTile(onHome = onHome)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = palette.textPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                item {
                    SettingsCard(heading = stringResource(R.string.settings_units)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            HudChip(
                                label = stringResource(R.string.unit_awg),
                                selected = state.sizeSystem == SizeSystem.AWG,
                                onClick = { onSizeSystem(SizeSystem.AWG) },
                                modifier = Modifier.weight(1f)
                            )
                            HudChip(
                                label = stringResource(R.string.unit_mm2),
                                selected = state.sizeSystem == SizeSystem.METRIC,
                                onClick = { onSizeSystem(SizeSystem.METRIC) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                item {
                    SettingsCard(heading = stringResource(R.string.settings_voltage)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GameConfig.VOLTAGE_OPTIONS.forEach { volts ->
                                HudChip(
                                    label = stringResource(R.string.fmt_volt_option, volts),
                                    selected = state.systemVoltage == volts,
                                    onClick = { onVoltage(volts) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                item {
                    SettingsCard(heading = stringResource(R.string.settings_material)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            HudChip(
                                label = stringResource(R.string.material_copper),
                                selected = state.material == ConductorMaterial.COPPER,
                                onClick = { onMaterial(ConductorMaterial.COPPER) },
                                accent = palette.copperEnd,
                                icon = painterResource(R.drawable.sprite_1),
                                iconDescription = stringResource(R.string.cd_copper),
                                modifier = Modifier.weight(1f)
                            )
                            HudChip(
                                label = stringResource(R.string.material_aluminium),
                                selected = state.material == ConductorMaterial.ALUMINIUM,
                                onClick = { onMaterial(ConductorMaterial.ALUMINIUM) },
                                accent = palette.aluminiumEnd,
                                icon = painterResource(R.drawable.sprite_2),
                                iconDescription = stringResource(R.string.cd_aluminium),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                item {
                    SettingsCard(heading = stringResource(R.string.settings_limit)) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(
                                    R.string.fmt_percent_int,
                                    state.lossLimitPercent
                                ),
                                style = numericStyle(26.sp),
                                color = palette.accent
                            )
                            Slider(
                                value = state.lossLimitPercent.toFloat(),
                                onValueChange = { raw -> onLossLimit(raw.roundToInt()) },
                                valueRange = 1f..10f,
                                steps = 8,
                                colors = SliderDefaults.colors(
                                    thumbColor = palette.accent,
                                    activeTrackColor = palette.accent,
                                    inactiveTrackColor = palette.surfaceAlt,
                                    activeTickColor = palette.bgDeep,
                                    inactiveTickColor = palette.border
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                item {
                    val onLabel = stringResource(R.string.state_on)
                    val offLabel = stringResource(R.string.state_off)
                    SettingsCard(heading = stringResource(R.string.settings_theme)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.settings_theme_hint),
                                style = MaterialTheme.typography.bodySmall,
                                color = palette.textSecondary,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Switch(
                                checked = state.lightMode,
                                onCheckedChange = onLightMode,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = palette.bgDeep,
                                    checkedTrackColor = palette.primary,
                                    uncheckedThumbColor = palette.textTertiary,
                                    uncheckedTrackColor = palette.surfaceAlt,
                                    uncheckedBorderColor = palette.border
                                ),
                                modifier = Modifier.semantics {
                                    stateDescription = if (state.lightMode) onLabel else offLabel
                                }
                            )
                        }
                    }
                }

                item {
                    SettingsCard(heading = stringResource(R.string.settings_storage)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.fmt_saved_count, state.savedRuns),
                                style = numericStyle(15.sp),
                                color = palette.textPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            SecondaryButton(
                                text = stringResource(R.string.cta_clear_runs),
                                onClick = onClear,
                                enabled = state.savedRuns > 0,
                                accent = palette.fail,
                                height = 48.dp
                            )
                        }
                    }
                }
            }

            SecondaryButton(
                text = stringResource(R.string.cta_menu),
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                height = 52.dp
            )
        }
    }
}

@Composable
private fun SettingsCard(
    heading: String,
    content: @Composable () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        corner = 14.dp,
        contentPadding = PaddingValues(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            SectionHeading(text = heading)
            content()
        }
    }
}
