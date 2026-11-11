package com.cabledrop.gauge.domain.model

data class DiagramNode(
    val load: LoadPoint,
    val voltage: Double,
    val cumulativeDropVolts: Double,
    val cumulativeLossPercent: Double
)
