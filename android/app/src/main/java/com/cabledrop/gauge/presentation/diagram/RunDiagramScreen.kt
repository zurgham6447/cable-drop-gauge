package com.cabledrop.gauge.presentation.diagram

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
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
import com.cabledrop.gauge.core.ui.HomeTile
import com.cabledrop.gauge.core.ui.HudBackground
import com.cabledrop.gauge.core.ui.SecondaryButton
import com.cabledrop.gauge.core.ui.SectionHeading
import com.cabledrop.gauge.core.ui.decimals
import com.cabledrop.gauge.core.ui.wireLabel
import com.cabledrop.gauge.domain.model.DiagramNode
import com.cabledrop.gauge.domain.model.LoadPoint
import com.cabledrop.gauge.presentation.dialog.LoadDialog

@Composable
fun RunDiagramScreen(
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    val diagramViewModel: RunDiagramViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by diagramViewModel.state.collectAsStateWithLifecycle()

    if (state.dialogVisible) {
        LoadDialog(
            initial = state.editing,
            onDismiss = diagramViewModel::dismissDialog,
            onSave = diagramViewModel::saveLoad
        )
    }

    RunDiagramContent(
        state = state,
        onBack = onBack,
        onHome = onHome,
        onAdd = diagramViewModel::openAdd,
        onEdit = diagramViewModel::openEdit,
        onRemove = diagramViewModel::removeLoad
    )
}

@Composable
private fun RunDiagramContent(
    state: RunDiagramUiState,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (LoadPoint) -> Unit,
    onRemove: (String) -> Unit
) {
    val palette = AppTheme.colors
    val transition = rememberInfiniteTransition(label = "diagram")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cablePulse"
    )

    HudBackground(
        overlay = listOf(
            palette.bgDeep.copy(alpha = 0.94f),
            palette.surfaceAlt.copy(alpha = 0.6f),
            palette.bgDeep.copy(alpha = 0.96f)
        ),
        gridAlpha = 0.22f,
        bracketAlpha = 0.35f
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
                    text = stringResource(R.string.diagram_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = palette.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onAdd, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.cta_add),
                        tint = palette.primary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                showBrackets = true,
                contentPadding = PaddingValues(12.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    SectionHeading(text = stringResource(R.string.diagram_cable))
                    CableTrack(
                        nodes = state.nodes,
                        span = state.cableLength,
                        pulse = pulse,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(188.dp)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    SectionHeading(text = stringResource(R.string.diagram_loads))
                    Spacer(modifier = Modifier.height(8.dp))
                    if (state.loads.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.sprite_4),
                                contentDescription = stringResource(R.string.cd_led),
                                modifier = Modifier.size(56.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.diagram_empty),
                                style = MaterialTheme.typography.bodySmall,
                                color = palette.textTertiary,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(items = state.loads, key = { entry -> entry.id }) { load ->
                                LoadRow(
                                    load = load,
                                    onEdit = { onEdit(load) },
                                    onRemove = { onRemove(load.id) }
                                )
                            }
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.fmt_diagram_totals,
                        state.loads.size,
                        decimals(state.totalWatts, 0),
                        decimals(state.endLossPercent, 1)
                    ),
                    style = numericStyle(12.sp),
                    color = if (state.withinLimit) palette.pass else palette.fail,
                    modifier = Modifier.weight(1f)
                )
                if (state.wireSize != null) {
                    Text(
                        text = wireLabel(state.wireSize, state.sizeSystem),
                        style = numericStyle(12.sp),
                        color = palette.accent
                    )
                }
            }

            SecondaryButton(
                text = stringResource(R.string.cta_back),
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                height = 48.dp
            )
        }
    }
}

@Composable
private fun CableTrack(
    nodes: List<DiagramNode>,
    span: Double,
    pulse: Float,
    modifier: Modifier = Modifier
) {
    val palette = AppTheme.colors
    BoxWithConstraints(modifier = modifier) {
        val trackStart = 60.dp
        val trackEnd = maxWidth - 18.dp
        val trackWidth = trackEnd - trackStart
        val cableY = 58.dp
        val endLoss = nodes.lastOrNull()?.cumulativeLossPercent ?: 0.0
        val fade = (endLoss / 20.0).coerceIn(0.0, 0.9).toFloat()

        Canvas(modifier = Modifier.fillMaxSize()) {
            val y = cableY.toPx()
            val startX = trackStart.toPx()
            val endX = trackEnd.toPx()
            val steps = 24
            for (index in 0 until steps) {
                val from = startX + (endX - startX) * index / steps
                val to = startX + (endX - startX) * (index + 1) / steps
                val mix = (index.toFloat() / steps) * fade
                drawLine(
                    color = lerp(palette.primary, palette.surfaceAlt, mix),
                    start = Offset(from, y),
                    end = Offset(to, y),
                    strokeWidth = 6.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            val headX = startX + (endX - startX) * pulse
            drawCircle(
                color = palette.primary.copy(alpha = (1f - fade * pulse).coerceIn(0.2f, 1f)),
                radius = 7.dp.toPx(),
                center = Offset(headX, y)
            )
        }

        Image(
            painter = painterResource(R.drawable.sprite_5),
            contentDescription = stringResource(R.string.cd_supply),
            modifier = Modifier
                .offset(x = 4.dp, y = cableY - 24.dp)
                .size(48.dp),
            contentScale = ContentScale.Fit
        )

        nodes.forEach { node ->
            val fraction = (node.load.distanceMeters / span).coerceIn(0.0, 1.0).toFloat()
            val centerX = trackStart + trackWidth * fraction
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(x = centerX - 30.dp, y = cableY - 18.dp)
                    .width(60.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(palette.accent.copy(alpha = 0.22f), CircleShape)
                        .border(1.dp, palette.accent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_4),
                        contentDescription = stringResource(R.string.cd_led),
                        modifier = Modifier.size(22.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.fmt_volts, decimals(node.voltage, 1)),
                    style = numericStyle(11.sp),
                    color = palette.textPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.fmt_watts, decimals(node.load.watts, 0)),
                    style = numericStyle(10.sp),
                    color = palette.textTertiary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LoadRow(
    load: LoadPoint,
    onEdit: () -> Unit,
    onRemove: () -> Unit
) {
    val palette = AppTheme.colors
    val shape = RoundedCornerShape(10.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(shape)
            .background(palette.surface.copy(alpha = 0.7f), shape)
            .border(1.dp, palette.border, shape)
            .clickable(onClick = onEdit)
            .padding(start = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = load.name,
                style = MaterialTheme.typography.bodyMedium,
                color = palette.textPrimary
            )
            Text(
                text = stringResource(
                    R.string.fmt_load_spec,
                    decimals(load.watts, 0),
                    decimals(load.distanceMeters, 1)
                ),
                style = numericStyle(11.sp),
                color = palette.textTertiary
            )
        }
        IconButton(onClick = onRemove, modifier = Modifier.size(48.dp)) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.cd_remove_load),
                tint = palette.fail,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
