package com.cabledrop.gauge.domain.model

data class WireSize(
    val awg: Int,
    val areaMm2: Double,
    val metricMm2: Double,
    val ampacityAmps: Double
)
