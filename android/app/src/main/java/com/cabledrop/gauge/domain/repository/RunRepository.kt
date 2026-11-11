package com.cabledrop.gauge.domain.repository

import com.cabledrop.gauge.domain.model.LoadPoint
import com.cabledrop.gauge.domain.model.WiringRun
import kotlinx.coroutines.flow.StateFlow

interface RunRepository {
    val savedRuns: StateFlow<List<WiringRun>>
    val activeRun: StateFlow<WiringRun?>
    val loadPoints: StateFlow<List<LoadPoint>>

    fun setActiveRun(run: WiringRun)
    fun saveRun(run: WiringRun)
    fun clearRuns()
    fun addLoadPoint(point: LoadPoint)
    fun updateLoadPoint(point: LoadPoint)
    fun removeLoadPoint(id: String)
}
