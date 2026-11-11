package com.cabledrop.gauge.presentation.gameover

import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.DropResult
import com.cabledrop.gauge.domain.model.FuseHint
import com.cabledrop.gauge.domain.model.PowerBudget
import com.cabledrop.gauge.domain.model.SizeSystem

data class GameOverUiState(
    val ready: Boolean = false,
    val runName: String = "",
    val sizeSystem: SizeSystem = SizeSystem.AWG,
    val material: ConductorMaterial = ConductorMaterial.COPPER,
    val chosen: DropResult? = null,
    val noSolution: Boolean = false,
    val fuse: FuseHint? = null,
    val budget: PowerBudget? = null,
    val currentAmps: Double = 0.0,
    val lossLimitPercent: Int = 3
)
