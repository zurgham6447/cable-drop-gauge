package com.cabledrop.gauge.presentation.gameover

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cabledrop.gauge.R
import com.cabledrop.gauge.core.di.ServiceLocator
import com.cabledrop.gauge.core.theme.AppTheme
import com.cabledrop.gauge.core.theme.numericStyle
import androidx.compose.foundation.layout.BoxScope
import com.cabledrop.gauge.core.ui.GlassCard
import com.cabledrop.gauge.core.ui.HudBackground
import com.cabledrop.gauge.core.ui.PrimaryButton
import com.cabledrop.gauge.core.ui.SafetyBanner
import com.cabledrop.gauge.core.ui.SecondaryButton
import com.cabledrop.gauge.core.ui.SectionHeading
import com.cabledrop.gauge.core.ui.StatCard
import com.cabledrop.gauge.core.ui.decimals
import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.SizeSystem

@Composable
fun GameOverScreen(
    onCalculateAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    GameOverContent(
        state = state,
        onCalculateAgain = onCalculateAgain,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    state: GameOverUiState,
    onCalculateAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val palette = AppTheme.colors
    val chosen = state.chosen
    val awgTarget = chosen?.size?.awg ?: 0
    val animatedAwg by animateIntAsState(
        targetValue = awgTarget,
        animationSpec = tween(500),
        label = "awgCount"
    )
    val fuse = state.fuse
    val budget = state.budget

    HudBackdrop {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.result_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = palette.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = state.runName,
                    style = numericStyle(12.sp),
                    color = palette.textTertiary
                )
            }

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp),
                borderColor = palette.accent.copy(alpha = 0.7f),
                borderWidth = 1.5.dp,
                corner = 18.dp,
                showBrackets = true,
                bracketColor = palette.accent,
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        SectionHeading(text = stringResource(R.string.result_recommended))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (chosen == null) {
                                stringResource(R.string.placeholder_value)
                            } else if (state.sizeSystem == SizeSystem.AWG) {
                                stringResource(R.string.fmt_awg, animatedAwg)
                            } else {
                                stringResource(
                                    R.string.fmt_mm2,
                                    decimals(chosen.size.metricMm2, 1)
                                )
                            },
                            style = numericStyle(38.sp),
                            color = palette.accent
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(
                                R.string.fmt_result_subline,
                                stringResource(
                                    if (state.material == ConductorMaterial.COPPER) {
                                        R.string.material_copper
                                    } else {
                                        R.string.material_aluminium
                                    }
                                ),
                                decimals(chosen?.lossPercent ?: 0.0, 1),
                                decimals(chosen?.dropVolts ?: 0.0, 2)
                            ),
                            style = numericStyle(13.sp),
                            color = palette.textPrimary
                        )
                        if (state.noSolution) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(R.string.result_no_solution),
                                style = MaterialTheme.typography.bodySmall,
                                color = palette.fail
                            )
                        }
                    }
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = stringResource(R.string.cd_copper),
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(bottom = 10.dp)
                            .size(62.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            if (fuse != null && budget != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(132.dp)
                ) {
                    StatCard(
                        value = stringResource(R.string.fmt_amps, decimals(fuse.amps, 1)),
                        label = stringResource(
                            if (fuse.limitedByWire) {
                                R.string.result_fuse_limited
                            } else {
                                R.string.result_fuse
                            }
                        ),
                        accent = palette.accent,
                        icon = painterResource(R.drawable.sprite_3),
                        iconDescription = stringResource(R.string.cd_fuse),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    StatCard(
                        value = stringResource(
                            R.string.fmt_watts,
                            decimals(budget.supplyWatts, 0)
                        ),
                        label = stringResource(R.string.result_supply),
                        accent = palette.primary,
                        icon = painterResource(R.drawable.sprite_5),
                        iconDescription = stringResource(R.string.cd_supply),
                        announce = true,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }

            if (budget != null) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(104.dp),
                    contentPadding = PaddingValues(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SectionHeading(
                                text = stringResource(R.string.result_total_load),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = stringResource(
                                    R.string.fmt_watts,
                                    decimals(budget.totalWatts, 0)
                                ),
                                style = numericStyle(15.sp),
                                color = palette.textPrimary
                            )
                        }
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                        ) {
                            val radius = CornerRadius(size.height / 2f, size.height / 2f)
                            drawRoundRect(
                                color = palette.surfaceAlt,
                                size = Size(size.width, size.height),
                                cornerRadius = radius
                            )
                            val supply = budget.supplyWatts.coerceAtLeast(1.0)
                            val loadFraction =
                                (budget.totalWatts / supply).coerceIn(0.0, 1.0).toFloat()
                            drawRoundRect(
                                color = palette.primary,
                                size = Size(size.width * loadFraction, size.height),
                                cornerRadius = radius
                            )
                            drawRoundRect(
                                color = palette.accent,
                                topLeft = Offset(size.width * loadFraction, 0f),
                                size = Size(size.width * (1f - loadFraction), size.height),
                                cornerRadius = radius
                            )
                        }
                        Text(
                            text = stringResource(R.string.result_headroom),
                            style = MaterialTheme.typography.labelSmall,
                            color = palette.textTertiary
                        )
                    }
                }
            }

            SafetyBanner(modifier = Modifier.fillMaxWidth())

            val breakdown = listOf(
                BreakdownEntry(
                    label = stringResource(R.string.readout_current),
                    value = stringResource(R.string.fmt_amps, decimals(state.currentAmps, 1))
                ),
                BreakdownEntry(
                    label = stringResource(R.string.readout_drop),
                    value = stringResource(
                        R.string.fmt_volts,
                        decimals(chosen?.dropVolts ?: 0.0, 2)
                    )
                ),
                BreakdownEntry(
                    label = stringResource(R.string.readout_loss),
                    value = stringResource(
                        R.string.fmt_percent,
                        decimals(chosen?.lossPercent ?: 0.0, 1)
                    ),
                    tone = if (state.noSolution) palette.fail else palette.pass
                ),
                BreakdownEntry(
                    label = stringResource(R.string.result_limit),
                    value = stringResource(R.string.fmt_percent_int, state.lossLimitPercent)
                ),
                BreakdownEntry(
                    label = stringResource(R.string.result_at_load),
                    value = stringResource(
                        R.string.fmt_volts,
                        decimals(chosen?.voltageAtLoad ?: 0.0, 2)
                    )
                ),
                BreakdownEntry(
                    label = stringResource(R.string.result_spare),
                    value = stringResource(
                        R.string.fmt_watts,
                        decimals(
                            ((budget?.supplyWatts ?: 0.0) - (budget?.totalWatts ?: 0.0))
                                .coerceAtLeast(0.0),
                            0
                        )
                    )
                ),
                BreakdownEntry(
                    label = stringResource(R.string.result_conductor),
                    value = stringResource(
                        if (state.material == ConductorMaterial.COPPER) {
                            R.string.material_copper
                        } else {
                            R.string.material_aluminium
                        }
                    )
                )
            )

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    SectionHeading(text = stringResource(R.string.result_breakdown))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        items(items = breakdown, key = { entry -> entry.label }) { entry ->
                            BreakdownRow(
                                label = entry.label,
                                value = entry.value,
                                tone = entry.tone,
                                modifier = Modifier.padding(vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            PrimaryButton(
                text = stringResource(R.string.cta_calculate_again),
                onClick = onCalculateAgain,
                gradient = listOf(palette.accent, palette.accentDim),
                modifier = Modifier.fillMaxWidth(),
                height = 58.dp
            )
            SecondaryButton(
                text = stringResource(R.string.cta_menu),
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth(),
                height = 48.dp
            )
        }
    }
}

private data class BreakdownEntry(
    val label: String,
    val value: String,
    val tone: Color? = null
)

@Composable
private fun BreakdownRow(
    label: String,
    value: String,
    tone: Color? = null,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color = palette.surfaceAlt.copy(alpha = 0.45f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = palette.textSecondary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = numericStyle(15.sp),
            color = tone ?: palette.textPrimary
        )
    }
}

@Composable
private fun HudBackdrop(content: @Composable BoxScope.() -> Unit) {
    val palette = AppTheme.colors
    HudBackground(
        overlay = listOf(
            palette.bgDeep.copy(alpha = 0.95f),
            palette.accent.copy(alpha = 0.10f),
            palette.bgDeep.copy(alpha = 0.95f)
        ),
        gridAlpha = 0.22f,
        bracketAlpha = 0.4f,
        content = content
    )
}
