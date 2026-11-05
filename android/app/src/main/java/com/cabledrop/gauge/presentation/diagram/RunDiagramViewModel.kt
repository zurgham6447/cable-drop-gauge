package com.cabledrop.gauge.presentation.diagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabledrop.gauge.core.config.GameConfig
import com.cabledrop.gauge.data.sample.SampleData
import com.cabledrop.gauge.domain.model.LoadPoint
import com.cabledrop.gauge.domain.repository.RunRepository
import com.cabledrop.gauge.domain.repository.SettingsRepository
import com.cabledrop.gauge.domain.usecase.CalculateDropUseCase
import com.cabledrop.gauge.domain.usecase.DiagramDropUseCase
import com.cabledrop.gauge.domain.usecase.RecommendWireUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RunDiagramViewModel(
    private val runRepository: RunRepository,
    settingsRepository: SettingsRepository,
    private val calculateDrop: CalculateDropUseCase,
    private val recommendWire: RecommendWireUseCase,
    private val diagramDrop: DiagramDropUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RunDiagramUiState())
    val state: StateFlow<RunDiagramUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                runRepository.loadPoints,
                runRepository.activeRun,
                settingsRepository.settings
            ) { loads, run, settings ->
                val voltage = run?.systemVoltage ?: settings.systemVoltage
                val material = run?.material ?: settings.material
                val size = run?.let { active ->
                    recommendWire(calculateDrop(active, SampleData.wireTable))?.size
                } ?: SampleData.wireTable[3]
                val nodes = diagramDrop(loads, voltage, material, size.areaMm2)
                val span = maxOf(
                    loads.maxOfOrNull { it.distanceMeters } ?: 0.0,
                    run?.lengthMeters ?: 0.0,
                    1.0
                )
                RunDiagramUiState(
                    loads = loads,
                    nodes = nodes,
                    systemVoltage = voltage,
                    totalWatts = loads.sumOf { it.watts },
                    endLossPercent = nodes.lastOrNull()?.cumulativeLossPercent ?: 0.0,
                    cableLength = span,
                    wireSize = size,
                    sizeSystem = settings.sizeSystem,
                    lossLimitPercent = run?.lossLimitPercent ?: settings.lossLimitPercent,
                    editing = _state.value.editing,
                    dialogVisible = _state.value.dialogVisible
                )
            }.collect { snapshot -> _state.value = snapshot }
        }
    }

    fun openAdd() {
        _state.value = _state.value.copy(editing = null, dialogVisible = true)
    }

    fun openEdit(point: LoadPoint) {
        _state.value = _state.value.copy(editing = point, dialogVisible = true)
    }

    fun dismissDialog() {
        _state.value = _state.value.copy(editing = null, dialogVisible = false)
    }

    fun saveLoad(name: String, watts: Double, distanceMeters: Double) {
        val current = _state.value
        val safeWatts = watts.coerceIn(GameConfig.MIN_LOAD_POINT_WATTS, GameConfig.MAX_LOAD_POINT_WATTS)
        val safeDistance =
            distanceMeters.coerceIn(GameConfig.MIN_DIAGRAM_DISTANCE, GameConfig.MAX_DIAGRAM_DISTANCE)
        val editing = current.editing
        if (editing == null) {
            runRepository.addLoadPoint(
                LoadPoint(
                    id = "load-" + System.currentTimeMillis(),
                    name = name,
                    watts = safeWatts,
                    distanceMeters = safeDistance
                )
            )
        } else {
            runRepository.updateLoadPoint(
                editing.copy(name = name, watts = safeWatts, distanceMeters = safeDistance)
            )
        }
        dismissDialog()
    }

    fun removeLoad(id: String) {
        runRepository.removeLoadPoint(id)
    }
}
