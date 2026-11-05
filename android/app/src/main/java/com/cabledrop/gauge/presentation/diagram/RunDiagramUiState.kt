package com.cabledrop.gauge.presentation.diagram

import com.cabledrop.gauge.domain.model.DiagramNode
import com.cabledrop.gauge.domain.model.LoadPoint
import com.cabledrop.gauge.domain.model.SizeSystem
import com.cabledrop.gauge.domain.model.WireSize

data class RunDiagramUiState(
    val loads: List<LoadPoint> = emptyList(),
    val nodes: List<DiagramNode> = emptyList(),
    val systemVoltage: Int = 12,
    val totalWatts: Double = 0.0,
    val endLossPercent: Double = 0.0,
    val cableLength: Double = 1.0,
    val wireSize: WireSize? = null,
    val sizeSystem: SizeSystem = SizeSystem.AWG,
    val lossLimitPercent: Int = 3,
    val editing: LoadPoint? = null,
    val dialogVisible: Boolean = false
) {
    val withinLimit: Boolean
        get() = endLossPercent <= lossLimitPercent
}
