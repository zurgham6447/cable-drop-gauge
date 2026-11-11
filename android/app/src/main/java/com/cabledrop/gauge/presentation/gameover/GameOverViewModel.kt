package com.cabledrop.gauge.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabledrop.gauge.data.sample.SampleData
import com.cabledrop.gauge.domain.repository.RunRepository
import com.cabledrop.gauge.domain.repository.SettingsRepository
import com.cabledrop.gauge.domain.usecase.CalculateDropUseCase
import com.cabledrop.gauge.domain.usecase.PowerBudgetUseCase
import com.cabledrop.gauge.domain.usecase.RecommendWireUseCase
import com.cabledrop.gauge.domain.usecase.SuggestFuseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GameOverViewModel(
    runRepository: RunRepository,
    settingsRepository: SettingsRepository,
    private val calculateDrop: CalculateDropUseCase,
    private val recommendWire: RecommendWireUseCase,
    private val suggestFuse: SuggestFuseUseCase,
    private val powerBudget: PowerBudgetUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                runRepository.activeRun,
                settingsRepository.settings
            ) { run, settings ->
                if (run == null) {
                    GameOverUiState()
                } else {
                    val results = calculateDrop(run, SampleData.wireTable)
                    val best = recommendWire(results)
                    val chosen = best ?: results.minByOrNull { it.lossPercent }
                    val fuse = chosen?.let {
                        suggestFuse(run.currentAmps, it.size.ampacityAmps, SampleData.fuseRatings)
                    }
                    GameOverUiState(
                        ready = chosen != null,
                        runName = run.name,
                        sizeSystem = settings.sizeSystem,
                        material = run.material,
                        chosen = chosen,
                        noSolution = best == null,
                        fuse = fuse,
                        budget = powerBudget(run.watts),
                        currentAmps = run.currentAmps,
                        lossLimitPercent = run.lossLimitPercent
                    )
                }
            }.collect { snapshot -> _state.value = snapshot }
        }
    }
}
