package com.cabledrop.gauge.data.repository

import com.cabledrop.gauge.data.local.AppPreferences
import com.cabledrop.gauge.data.sample.SampleData
import com.cabledrop.gauge.domain.model.LoadPoint
import com.cabledrop.gauge.domain.model.WiringRun
import com.cabledrop.gauge.domain.repository.RunRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RunRepositoryImpl(private val preferences: AppPreferences) : RunRepository {

    private val _savedRuns = MutableStateFlow<List<WiringRun>>(emptyList())
    override val savedRuns: StateFlow<List<WiringRun>> = _savedRuns.asStateFlow()

    private val _activeRun = MutableStateFlow<WiringRun?>(null)
    override val activeRun: StateFlow<WiringRun?> = _activeRun.asStateFlow()

    private val _loadPoints = MutableStateFlow<List<LoadPoint>>(emptyList())
    override val loadPoints: StateFlow<List<LoadPoint>> = _loadPoints.asStateFlow()

    init {
        if (!preferences.isSeeded()) {
            preferences.writeRuns(SampleData.seededRuns)
            preferences.writeLoadPoints(SampleData.seededLoadPoints)
            preferences.markSeeded()
        }
        _savedRuns.value = preferences.readRuns()
        _loadPoints.value = preferences.readLoadPoints().sortedBy { it.distanceMeters }
        val activeId = preferences.readActiveRunId()
        _activeRun.value = _savedRuns.value.firstOrNull { it.id == activeId }
    }

    override fun setActiveRun(run: WiringRun) {
        _activeRun.value = run
        preferences.writeActiveRunId(run.id)
    }

    override fun saveRun(run: WiringRun) {
        val merged = _savedRuns.value.filter { it.id != run.id } + run
        val trimmed = merged.takeLast(MAX_RUNS)
        _savedRuns.value = trimmed
        preferences.writeRuns(trimmed)
    }

    override fun clearRuns() {
        _savedRuns.value = emptyList()
        _activeRun.value = null
        preferences.writeRuns(emptyList())
        preferences.writeActiveRunId("")
    }

    override fun addLoadPoint(point: LoadPoint) {
        persistLoads(_loadPoints.value + point)
    }

    override fun updateLoadPoint(point: LoadPoint) {
        persistLoads(_loadPoints.value.map { if (it.id == point.id) point else it })
    }

    override fun removeLoadPoint(id: String) {
        persistLoads(_loadPoints.value.filterNot { it.id == id })
    }

    private fun persistLoads(points: List<LoadPoint>) {
        val sorted = points.sortedBy { it.distanceMeters }
        _loadPoints.value = sorted
        preferences.writeLoadPoints(sorted)
    }

    private companion object {
        const val MAX_RUNS = 12
    }
}
