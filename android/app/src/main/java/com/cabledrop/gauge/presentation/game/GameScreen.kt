package com.cabledrop.gauge.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
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
import com.cabledrop.gauge.core.ui.HudStepper
import com.cabledrop.gauge.core.ui.PrimaryButton
import com.cabledrop.gauge.core.ui.SecondaryButton
import com.cabledrop.gauge.core.ui.SectionHeading
import com.cabledrop.gauge.core.ui.WireRow
import com.cabledrop.gauge.core.ui.decimals
import com.cabledrop.gauge.core.ui.wireLabel
import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.LoadUnit

@Composable
fun GameScreen(
    onHome: () -> Unit,
    onDiagram: () -> Unit,
    onCalculated: (String) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(gameViewModel) {
        gameViewModel.events.collect { runId -> onCalculated(runId) }
    }

    DisposableEffect(gameViewModel) {
        onDispose { gameViewModel.pause() }
    }

    GameContent(
        state = state,
        onHome = onHome,
        onDiagram = onDiagram,
        onToggleHold = gameViewModel::toggleHold,
        onVoltage = gameViewModel::setVoltage,
        onMaterial = gameViewModel::setMaterial,
        onLossLimit = gameViewModel::setLossLimit,
        onLoadUnit = gameViewModel::setLoadUnit,
        onStepStart = gameViewModel::startStep,
        onStepStop = gameViewModel::stopStep,
        onCalculate = gameViewModel::calculate
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onHome: () -> Unit,
    onDiagram: () -> Unit,
    onToggleHold: () -> Unit,
    onVoltage: (Int) -> Unit,
    onMaterial: (ConductorMaterial) -> Unit,
    onLossLimit: (Int) -> Unit,
    onLoadUnit: (LoadUnit) -> Unit,
    onStepStart: (StepField, Int) -> Unit,
    onStepStop: () -> Unit,
    onCalculate: () -> Unit
) {
    val palette = AppTheme.colors
    val placeholder = stringResource(R.string.placeholder_value)

    HudBackground(
        overlay = listOf(
            palette.bgDeep.copy(alpha = 0.92f),
            palette.bgBase.copy(alpha = 0.82f),
            palette.bgDeep.copy(alpha = 0.95f)
        ),
        gridAlpha = 0.2f,
        bracketAlpha = 0.3f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(palette.surface.copy(alpha = 0.88f))
                    .padding(horizontal = 12.dp)
            ) {
                HomeTile(onHome = onHome)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.game_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = palette.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                HoldToggle(held = state.held, onToggle = onToggleHold)
            }

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                showBrackets = true,
                contentPadding = PaddingValues(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionHeading(text = stringResource(R.string.game_system))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        GameConfig.VOLTAGE_OPTIONS.forEach { volts ->
                            HudChip(
                                label = stringResource(R.string.fmt_volt_option, volts),
                                selected = state.systemVoltage == volts,
                                onClick = { onVoltage(volts) },
                                height = 48.dp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HudChip(
                            label = stringResource(R.string.material_copper),
                            selected = state.material == ConductorMaterial.COPPER,
                            onClick = { onMaterial(ConductorMaterial.COPPER) },
                            height = 48.dp,
                            icon = painterResource(R.drawable.sprite_1),
                            iconDescription = stringResource(R.string.cd_copper),
                            modifier = Modifier.weight(1f)
                        )
                        HudChip(
                            label = stringResource(R.string.material_aluminium),
                            selected = state.material == ConductorMaterial.ALUMINIUM,
                            onClick = { onMaterial(ConductorMaterial.ALUMINIUM) },
                            height = 48.dp,
                            icon = painterResource(R.drawable.sprite_2),
                            iconDescription = stringResource(R.string.cd_aluminium),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        GameConfig.LOSS_LIMIT_OPTIONS.forEach { limit ->
                            HudChip(
                                label = stringResource(R.string.fmt_percent_int, limit),
                                selected = state.lossLimitPercent == limit,
                                onClick = { onLossLimit(limit) },
                                accent = palette.accent,
                                height = 48.dp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(152.dp)
                    .padding(horizontal = 16.dp)
            ) {
                HudStepper(
                    label = stringResource(R.string.game_load),
                    value = if (state.loadValid) decimals(state.loadValue, 1) else placeholder,
                    unit = if (state.loadUnit == LoadUnit.AMPS) {
                        stringResource(R.string.unit_amps)
                    } else {
                        stringResource(R.string.unit_watts)
                    },
                    minusDescription = stringResource(R.string.cd_load_minus),
                    plusDescription = stringResource(R.string.cd_load_plus),
                    onStepStart = { direction -> onStepStart(StepField.LOAD, direction) },
                    onStepStop = onStepStop,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    trailing = {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            HudChip(
                                label = stringResource(R.string.unit_watts),
                                selected = state.loadUnit == LoadUnit.WATTS,
                                onClick = { onLoadUnit(LoadUnit.WATTS) },
                                height = 48.dp,
                                modifier = Modifier.weight(1f)
                            )
                            HudChip(
                                label = stringResource(R.string.unit_amps),
                                selected = state.loadUnit == LoadUnit.AMPS,
                                onClick = { onLoadUnit(LoadUnit.AMPS) },
                                height = 48.dp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                )
                HudStepper(
                    label = stringResource(R.string.game_length),
                    value = if (state.lengthValid) decimals(state.lengthMeters, 1) else placeholder,
                    unit = stringResource(R.string.unit_meters),
                    minusDescription = stringResource(R.string.cd_length_minus),
                    plusDescription = stringResource(R.string.cd_length_plus),
                    onStepStart = { direction -> onStepStart(StepField.LENGTH, direction) },
                    onStepStop = onStepStop,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    trailing = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(palette.surfaceAlt.copy(alpha = 0.45f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.game_one_way),
                                style = MaterialTheme.typography.labelSmall,
                                color = palette.textTertiary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                if (state.valid && !state.anyPass) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = palette.fail,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .background(
                                    color = palette.fail.copy(alpha = 0.10f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.game_no_solution),
                                style = MaterialTheme.typography.bodySmall,
                                color = palette.fail
                            )
                        }
                    }
                }
                items(items = state.results, key = { entry -> entry.size.awg }) { result ->
                    WireRow(
                        gaugeLabel = wireLabel(result.size, state.sizeSystem),
                        areaMm2 = result.size.areaMm2,
                        copper = state.material == ConductorMaterial.COPPER,
                        dropLabel = stringResource(
                            R.string.fmt_volts,
                            decimals(result.dropVolts, 2)
                        ),
                        percentLabel = stringResource(
                            R.string.fmt_percent,
                            decimals(result.lossPercent, 1)
                        ),
                        verdictLabel = stringResource(
                            if (result.passes) R.string.verdict_pass else R.string.verdict_fail
                        ),
                        bestLabel = stringResource(R.string.label_best),
                        pass = result.passes,
                        recommended = state.recommended?.size?.awg == result.size.awg,
                        dimmed = !state.valid
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(palette.surface.copy(alpha = 0.92f))
                    .padding(horizontal = 16.dp)
            ) {
                ReadoutCell(
                    label = stringResource(R.string.readout_current),
                    value = if (state.valid) {
                        stringResource(R.string.fmt_amps, decimals(state.currentAmps, 1))
                    } else {
                        placeholder
                    },
                    tone = palette.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                CellDivider()
                ReadoutCell(
                    label = stringResource(R.string.readout_drop),
                    value = if (state.valid && state.recommended != null) {
                        stringResource(
                            R.string.fmt_volts,
                            decimals(state.recommended.dropVolts, 2)
                        )
                    } else {
                        placeholder
                    },
                    tone = palette.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                CellDivider()
                ReadoutCell(
                    label = stringResource(R.string.readout_loss),
                    value = if (state.valid && state.recommended != null) {
                        stringResource(
                            R.string.fmt_percent,
                            decimals(state.recommended.lossPercent, 1)
                        )
                    } else {
                        placeholder
                    },
                    tone = if (state.anyPass) palette.pass else palette.fail,
                    announce = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SecondaryButton(
                        text = stringResource(R.string.cta_run_diagram),
                        onClick = onDiagram,
                        modifier = Modifier.weight(1f),
                        height = 56.dp
                    )
                    PrimaryButton(
                        text = stringResource(R.string.cta_calculate),
                        onClick = onCalculate,
                        enabled = state.valid,
                        leadingIcon = ImageVector.vectorResource(R.drawable.ic_play_arrow),
                        modifier = Modifier.weight(1.5f),
                        height = 56.dp
                    )
                }
                Text(
                    text = stringResource(R.string.game_mains_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = palette.textTertiary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun HoldToggle(held: Boolean, onToggle: () -> Unit) {
    val palette = AppTheme.colors
    val shape = RoundedCornerShape(10.dp)
    val description = stringResource(R.string.cd_hold)
    val liveLabel = stringResource(R.string.state_live)
    val heldLabel = stringResource(R.string.state_held)
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(shape)
            .background(
                color = if (held) {
                    palette.accent.copy(alpha = 0.18f)
                } else {
                    palette.surfaceAlt
                },
                shape = shape
            )
            .border(
                width = 1.dp,
                color = if (held) palette.accent else palette.border,
                shape = shape
            )
            .clickable(onClickLabel = description, onClick = onToggle)
            .semantics {
                role = Role.Button
                stateDescription = if (held) heldLabel else liveLabel
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.icon_pause),
            contentDescription = description,
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun ReadoutCell(
    label: String,
    value: String,
    tone: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    announce: Boolean = false
) {
    val palette = AppTheme.colors
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = palette.textTertiary
        )
        Text(
            text = value,
            style = numericStyle(14.sp),
            color = tone,
            modifier = if (announce) {
                Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            } else {
                Modifier
            }
        )
    }
}

@Composable
private fun CellDivider() {
    val palette = AppTheme.colors
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(28.dp)
            .background(palette.border)
    )
}
