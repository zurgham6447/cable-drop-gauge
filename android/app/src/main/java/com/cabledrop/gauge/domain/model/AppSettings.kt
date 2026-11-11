package com.cabledrop.gauge.domain.model

data class AppSettings(
    val sizeSystem: SizeSystem,
    val lossLimitPercent: Int,
    val material: ConductorMaterial,
    val systemVoltage: Int,
    val lightMode: Boolean
)
