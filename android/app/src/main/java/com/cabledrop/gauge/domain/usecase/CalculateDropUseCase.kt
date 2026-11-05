package com.cabledrop.gauge.domain.usecase

import com.cabledrop.gauge.domain.model.DropResult
import com.cabledrop.gauge.domain.model.WireSize
import com.cabledrop.gauge.domain.model.WiringRun

class CalculateDropUseCase {

    operator fun invoke(run: WiringRun, sizes: List<WireSize>): List<DropResult> {
        val current = run.currentAmps
        val loopLength = 2.0 * run.lengthMeters
        return sizes.map { size ->
            val drop = current * run.material.resistivity * loopLength / size.areaMm2
            val percent = drop / run.systemVoltage * 100.0
            DropResult(
                size = size,
                dropVolts = drop,
                lossPercent = percent,
                voltageAtLoad = run.systemVoltage - drop,
                withinLimit = percent <= run.lossLimitPercent,
                ampacityOk = size.ampacityAmps >= current
            )
        }
    }
}
