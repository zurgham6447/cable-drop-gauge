package com.cabledrop.gauge.domain.model

data class WiringRun(
    val id: String,
    val name: String,
    val systemVoltage: Int,
    val material: ConductorMaterial,
    val loadValue: Double,
    val loadUnit: LoadUnit,
    val lengthMeters: Double,
    val lossLimitPercent: Int
) {
    val currentAmps: Double
        get() = if (loadUnit == LoadUnit.AMPS) loadValue else loadValue / systemVoltage

    val watts: Double
        get() = if (loadUnit == LoadUnit.WATTS) loadValue else loadValue * systemVoltage
}
