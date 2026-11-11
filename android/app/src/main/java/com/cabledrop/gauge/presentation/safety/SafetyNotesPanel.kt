package com.cabledrop.gauge.presentation.safety

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.cabledrop.gauge.core.ui.SafetyBanner
import com.cabledrop.gauge.core.ui.SecondaryButton
import com.cabledrop.gauge.domain.model.SafetyNote

@Composable
fun SafetyNotesPanel(
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    val safetyViewModel: SafetyNotesViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by safetyViewModel.state.collectAsStateWithLifecycle()

    SafetyNotesContent(
        state = state,
        onBack = onBack,
        onHome = onHome
    )
}

@Composable
private fun SafetyNotesContent(
    state: SafetyNotesUiState,
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    val palette = AppTheme.colors

    HudBackground(
        overlay = listOf(
            palette.bgBase.copy(alpha = 0.93f),
            palette.fail.copy(alpha = 0.10f),
            palette.bgDeep.copy(alpha = 0.96f)
        ),
        gridAlpha = 0.20f
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
                    text = stringResource(R.string.safety_title),
                    style = MaterialTheme.typography.titleSmall,
                    color = palette.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_fuse),
                    modifier = Modifier.size(34.dp),
                    contentScale = ContentScale.Fit
                )
            }

            SafetyBanner(modifier = Modifier.fillMaxWidth())

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly,
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(items = state.notes, key = { note -> note.number }) { note ->
                    SafetyNoteCard(note = note, modifier = Modifier.padding(vertical = 5.dp))
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
private fun SafetyNoteCard(note: SafetyNote, modifier: Modifier = Modifier) {
    val palette = AppTheme.colors
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = palette.border,
        corner = 14.dp,
        contentPadding = PaddingValues(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(
                            color = palette.accent.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = note.number.toString(),
                        style = numericStyle(12.sp),
                        color = palette.accent
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(note.titleRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = palette.primary,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                text = stringResource(note.bodyRes),
                style = MaterialTheme.typography.bodyMedium,
                color = palette.textSecondary
            )
        }
    }
}
