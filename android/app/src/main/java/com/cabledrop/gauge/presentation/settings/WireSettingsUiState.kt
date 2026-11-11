package com.cabledrop.gauge.presentation.settings

import com.cabledrop.gauge.core.config.GameConfig
import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.SizeSystem

data class WireSettingsUiState(
    val sizeSystem: SizeSystem = SizeSystem.AWG,
    val lossLimitPercent: Int = GameConfig.DEFAULT_LOSS_LIMIT,
    val material: ConductorMaterial = ConductorMaterial.COPPER,
    val systemVoltage: Int = GameConfig.DEFAULT_VOLTAGE,
    val lightMode: Boolean = false,
    val savedRuns: Int = 0,
    val confirmVisible: Boolean = false
)
