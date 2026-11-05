package com.cabledrop.gauge.domain.model

data class DropResult(
    val size: WireSize,
    val dropVolts: Double,
    val lossPercent: Double,
    val voltageAtLoad: Double,
    val withinLimit: Boolean,
    val ampacityOk: Boolean
) {
    val passes: Boolean
        get() = withinLimit && ampacityOk
}
