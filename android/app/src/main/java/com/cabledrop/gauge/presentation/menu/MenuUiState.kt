package com.cabledrop.gauge.presentation.menu

import com.cabledrop.gauge.domain.model.SizeSystem
import com.cabledrop.gauge.domain.model.WireSize
import com.cabledrop.gauge.domain.model.WiringRun

data class MenuUiState(
    val savedRuns: List<WiringRun> = emptyList(),
    val chosenSizes: Map<String, WireSize> = emptyMap(),
    val lastLossPercent: Double = 0.0,
    val hasActiveRun: Boolean = false,
    val sizeSystem: SizeSystem = SizeSystem.AWG
)
