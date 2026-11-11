package com.cabledrop.gauge.presentation.game

import com.cabledrop.gauge.core.config.GameConfig
import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.DropResult
import com.cabledrop.gauge.domain.model.LoadUnit
import com.cabledrop.gauge.domain.model.SizeSystem

enum class StepField {
    LOAD,
    LENGTH
}

data class GameUiState(
    val systemVoltage: Int = GameConfig.DEFAULT_VOLTAGE,
    val material: ConductorMaterial = ConductorMaterial.COPPER,
    val lossLimitPercent: Int = GameConfig.DEFAULT_LOSS_LIMIT,
    val loadValue: Double = GameConfig.DEFAULT_LOAD_WATTS,
    val loadUnit: LoadUnit = LoadUnit.WATTS,
    val lengthMeters: Double = GameConfig.DEFAULT_LENGTH_METERS,
    val sizeSystem: SizeSystem = SizeSystem.AWG,
    val held: Boolean = false,
    val results: List<DropResult> = emptyList(),
    val recommended: DropResult? = null,
    val currentAmps: Double = 0.0
) {
    val loadValid: Boolean
        get() = if (loadUnit == LoadUnit.AMPS) {
            loadValue >= GameConfig.MIN_LOAD_AMPS
        } else {
            loadValue >= GameConfig.MIN_LOAD_WATTS
        }

    val lengthValid: Boolean
        get() = lengthMeters >= GameConfig.MIN_LENGTH_METERS

    val valid: Boolean
        get() = loadValid && lengthValid

    val anyPass: Boolean
        get() = results.any { it.passes }
}
