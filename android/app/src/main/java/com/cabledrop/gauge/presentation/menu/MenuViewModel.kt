package com.cabledrop.gauge.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabledrop.gauge.data.sample.SampleData
import com.cabledrop.gauge.domain.model.WireSize
import com.cabledrop.gauge.domain.model.WiringRun
import com.cabledrop.gauge.domain.repository.RunRepository
import com.cabledrop.gauge.domain.repository.SettingsRepository
import com.cabledrop.gauge.domain.usecase.CalculateDropUseCase
import com.cabledrop.gauge.domain.usecase.RecommendWireUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MenuViewModel(
    private val runRepository: RunRepository,
    settingsRepository: SettingsRepository,
    private val calculateDrop: CalculateDropUseCase,
    private val recommendWire: RecommendWireUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                runRepository.savedRuns,
                runRepository.activeRun,
                settingsRepository.settings
            ) { runs, active, settings ->
                val chosen = mutableMapOf<String, WireSize>()
                runs.forEach { run ->
                    bestFor(run)?.let { chosen[run.id] = it.first }
                }
                MenuUiState(
                    savedRuns = runs.reversed(),
                    chosenSizes = chosen,
                    lastLossPercent = active?.let { bestFor(it)?.second } ?: 0.0,
                    hasActiveRun = active != null,
                    sizeSystem = settings.sizeSystem
                )
            }.collect { snapshot -> _state.value = snapshot }
        }
    }

    fun openRun(run: WiringRun) {
        runRepository.setActiveRun(run)
    }

    private fun bestFor(run: WiringRun): Pair<WireSize, Double>? {
        val results = calculateDrop(run, SampleData.wireTable)
        val best = recommendWire(results) ?: return null
        return best.size to best.lossPercent
    }
}
