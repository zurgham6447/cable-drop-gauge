package com.cabledrop.gauge.presentation.settings

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun WireSettingsScreen(
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
