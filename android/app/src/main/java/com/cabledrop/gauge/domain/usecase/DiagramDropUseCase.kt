package com.cabledrop.gauge.domain.usecase

import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.DiagramNode
import com.cabledrop.gauge.domain.model.LoadPoint

class DiagramDropUseCase {

    operator fun invoke(
        loads: List<LoadPoint>,
        systemVoltage: Int,
        material: ConductorMaterial,
        areaMm2: Double
    ): List<DiagramNode> {
        if (loads.isEmpty() || areaMm2 <= 0.0) return emptyList()
        val ordered = loads.sortedBy { it.distanceMeters }
        val currents = ordered.map { it.watts / systemVoltage }
        var previousDistance = 0.0
        var accumulated = 0.0
        val nodes = mutableListOf<DiagramNode>()
        ordered.forEachIndexed { index, load ->
            val downstream = currents.drop(index).sum()
            val segment = (load.distanceMeters - previousDistance).coerceAtLeast(0.0)
            accumulated += downstream * material.resistivity * 2.0 * segment / areaMm2
            nodes += DiagramNode(
                load = load,
                voltage = systemVoltage - accumulated,
                cumulativeDropVolts = accumulated,
                cumulativeLossPercent = accumulated / systemVoltage * 100.0
            )
            previousDistance = load.distanceMeters
        }
        return nodes
    }
}
