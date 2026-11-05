package com.cabledrop.gauge.presentation.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.cabledrop.gauge.core.ui.GlassCard
import com.cabledrop.gauge.core.ui.HudBackground
import com.cabledrop.gauge.core.ui.PrimaryButton
import com.cabledrop.gauge.core.ui.SecondaryButton
import com.cabledrop.gauge.core.ui.SectionHeading
import com.cabledrop.gauge.core.ui.StatCard
import com.cabledrop.gauge.core.ui.compactAmps
import com.cabledrop.gauge.core.ui.decimals
import com.cabledrop.gauge.core.ui.wireLabel
import com.cabledrop.gauge.domain.model.WiringRun

@Composable
fun MenuScreen(
    onStartRun: () -> Unit,
    onDiagram: () -> Unit,
    onSettings: () -> Unit,
    onSafety: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    MenuContent(
        state = state,
        onStartRun = onStartRun,
        onOpenRun = { run ->
            menuViewModel.openRun(run)
            onStartRun()
        },
        onDiagram = onDiagram,
        onSettings = onSettings,
        onSafety = onSafety
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onStartRun: () -> Unit,
    onOpenRun: (WiringRun) -> Unit,
    onDiagram: () -> Unit,
    onSettings: () -> Unit,
    onSafety: () -> Unit
) {
    val palette = AppTheme.colors
    val transition = rememberInfiniteTransition(label = "menu")
    val borderPulse by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ctaBorder"
    )

    HudBackground(
        overlay = listOf(
            palette.bgBase.copy(alpha = 0.90f),
            palette.surfaceAlt.copy(alpha = 0.70f),
            palette.bgBase.copy(alpha = 0.95f)
        ),
        gridAlpha = 0.25f
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
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    modifier = Modifier.size(42.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.app_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = palette.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onSettings, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.cd_settings),
                        tint = palette.textSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                GlassCard(
                    modifier = Modifier
                        .weight(1.6f)
                        .fillMaxHeight(),
                    borderColor = palette.primary.copy(alpha = borderPulse),
                    borderWidth = 1.5.dp,
                    corner = 18.dp,
                    fill = Brush.linearGradient(
                        listOf(
                            palette.primary.copy(alpha = 0.20f),
                            palette.surfaceAlt.copy(alpha = 0.72f)
                        )
                    ),
                    showBrackets = true,
                    contentPadding = PaddingValues(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_1),
                            contentDescription = stringResource(R.string.cd_copper),
                            modifier = Modifier.size(46.dp),
                            contentScale = ContentScale.Fit
                        )
                        Column {
                            SectionHeading(text = stringResource(R.string.menu_new_run))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.menu_tagline),
                                style = MaterialTheme.typography.bodySmall,
                                color = palette.textSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            PrimaryButton(
                                text = stringResource(R.string.cta_start_run),
                                onClick = onStartRun,
                                modifier = Modifier.fillMaxWidth(),
                                height = 56.dp
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (state.savedRuns.isNotEmpty() && state.lastLossPercent > 0.0) {
                        StatCard(
                            value = decimals(state.savedRuns.size.toDouble(), 0),
                            label = stringResource(R.string.menu_saved_runs),
                            accent = palette.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                        StatCard(
                            value = stringResource(
                                R.string.fmt_percent,
                                decimals(state.lastLossPercent, 1)
                            ),
                            label = stringResource(R.string.menu_last_drop),
                            accent = palette.accent,
                            announce = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    } else {
                        GlassCard(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.sprite_5),
                                    contentDescription = stringResource(R.string.cd_supply),
                                    modifier = Modifier.size(64.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = stringResource(R.string.menu_no_runs),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = palette.textTertiary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(12.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    SectionHeading(text = stringResource(R.string.menu_saved_section))
                    Spacer(modifier = Modifier.height(8.dp))
                    if (state.savedRuns.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.sprite_4),
                                contentDescription = stringResource(R.string.cd_led),
                                modifier = Modifier.size(72.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = stringResource(R.string.menu_empty_runs),
                                style = MaterialTheme.typography.bodyMedium,
                                color = palette.textTertiary
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(items = state.savedRuns, key = { entry -> entry.id }) { run ->
                                RunRow(
                                    run = run,
                                    badge = state.chosenSizes[run.id]?.let { size ->
                                        wireLabel(size, state.sizeSystem)
                                    },
                                    onClick = { onOpenRun(run) }
                                )
                            }
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SecondaryButton(
                    text = stringResource(R.string.cta_run_diagram),
                    onClick = onDiagram,
                    enabled = state.hasActiveRun,
                    modifier = Modifier.weight(1f),
                    height = 52.dp
                )
                SecondaryButton(
                    text = stringResource(R.string.cta_safety_notes),
                    onClick = onSafety,
                    modifier = Modifier.weight(1f),
                    height = 52.dp
                )
            }
        }
    }
}

@Composable
private fun RunRow(
    run: WiringRun,
    badge: String?,
    onClick: () -> Unit
) {
    val palette = AppTheme.colors
    val shape = RoundedCornerShape(10.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(shape)
            .background(color = palette.surface.copy(alpha = 0.7f), shape = shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = run.name,
                style = MaterialTheme.typography.bodyMedium,
                color = palette.textPrimary
            )
            Text(
                text = stringResource(
                    R.string.fmt_run_spec,
                    run.systemVoltage,
                    compactAmps(run.currentAmps),
                    decimals(run.lengthMeters, 1)
                ),
                style = numericStyle(11.sp),
                color = palette.textTertiary
            )
        }
        if (badge != null) {
            Box(
                modifier = Modifier
                    .background(
                        color = palette.accent.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = badge,
                    style = numericStyle(12.sp),
                    color = palette.accent
                )
            }
        }
    }
}
