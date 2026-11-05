package com.cabledrop.gauge.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabledrop.gauge.core.config.GameConfig
import com.cabledrop.gauge.data.sample.SampleData
import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.LoadUnit
import com.cabledrop.gauge.domain.model.WiringRun
import com.cabledrop.gauge.domain.repository.RunRepository
import com.cabledrop.gauge.domain.repository.SettingsRepository
import com.cabledrop.gauge.domain.usecase.CalculateDropUseCase
import com.cabledrop.gauge.domain.usecase.RecommendWireUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(
    private val runRepository: RunRepository,
    private val settingsRepository: SettingsRepository,
    private val calculateDrop: CalculateDropUseCase,
    private val recommendWire: RecommendWireUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val calculated = Channel<String>(Channel.BUFFERED)
    val events: Flow<String> = calculated.receiveAsFlow()

    private var repeatJob: Job? = null
    private var defaultsApplied = false

    init {
        _state.update { it }
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _state.update { current ->
                    if (defaultsApplied) {
                        current.copy(sizeSystem = settings.sizeSystem)
                    } else {
                        defaultsApplied = true
                        current.copy(
                            sizeSystem = settings.sizeSystem,
                            systemVoltage = settings.systemVoltage,
                            material = settings.material,
                            lossLimitPercent = settings.lossLimitPercent
                        )
                    }
                }
            }
        }
        viewModelScope.launch {
            runRepository.activeRun.collect { run ->
                if (run != null) {
                    _state.update { current ->
                        current.copy(
                            systemVoltage = run.systemVoltage,
                            material = run.material,
                            lossLimitPercent = run.lossLimitPercent,
                            loadValue = run.loadValue,
                            loadUnit = run.loadUnit,
                            lengthMeters = run.lengthMeters
                        )
                    }
                }
            }
        }
    }

    fun setVoltage(volts: Int) = _state.update { it.copy(systemVoltage = volts) }

    fun setMaterial(material: ConductorMaterial) = _state.update { it.copy(material = material) }

    fun setLossLimit(percent: Int) = _state.update { it.copy(lossLimitPercent = percent) }

    fun setLoadUnit(unit: LoadUnit) {
        _state.update { current ->
            if (current.loadUnit == unit) return@update current
            val converted = if (unit == LoadUnit.AMPS) {
                current.loadValue / current.systemVoltage
            } else {
                current.loadValue * current.systemVoltage
            }
            current.copy(loadUnit = unit, loadValue = roundStep(converted, unit))
        }
    }

    fun toggleHold() = _state.update { it.copy(held = !it.held) }

    fun startStep(field: StepField, direction: Int) {
        applyStep(field, direction)
        repeatJob?.cancel()
        repeatJob = viewModelScope.launch {
            delay(GameConfig.HOLD_REPEAT_DELAY_MS)
            while (isActive) {
                applyStep(field, direction)
                delay(GameConfig.HOLD_REPEAT_INTERVAL_MS)
            }
        }
    }

    fun stopStep() {
        repeatJob?.cancel()
        repeatJob = null
    }

    fun pause() = stopStep()

    fun calculate() {
        val current = _state.value
        if (!current.valid) return
        val index = runRepository.savedRuns.value.size + 1
        val run = WiringRun(
            id = "run-" + System.currentTimeMillis(),
            name = "RUN " + index.toString().padStart(2, '0'),
            systemVoltage = current.systemVoltage,
            material = current.material,
            loadValue = current.loadValue,
            loadUnit = current.loadUnit,
            lengthMeters = current.lengthMeters,
            lossLimitPercent = current.lossLimitPercent
        )
        runRepository.saveRun(run)
        runRepository.setActiveRun(run)
        calculated.trySend(run.id)
    }

    override fun onCleared() {
        super.onCleared()
        stopStep()
    }

    private fun applyStep(field: StepField, direction: Int) {
        _state.update { current ->
            when (field) {
                StepField.LOAD -> {
                    val step = if (current.loadUnit == LoadUnit.AMPS) {
                        GameConfig.LOAD_STEP_AMPS
                    } else {
                        GameConfig.LOAD_STEP_WATTS
                    }
                    val max = if (current.loadUnit == LoadUnit.AMPS) {
                        GameConfig.MAX_LOAD_AMPS
                    } else {
                        GameConfig.MAX_LOAD_WATTS
                    }
                    current.copy(
                        loadValue = (current.loadValue + direction * step).coerceIn(0.0, max)
                    )
                }

                StepField.LENGTH -> current.copy(
                    lengthMeters = (current.lengthMeters + direction * GameConfig.LENGTH_STEP)
                        .coerceIn(0.0, GameConfig.MAX_LENGTH_METERS)
                )
            }
        }
    }

    private fun roundStep(value: Double, unit: LoadUnit): Double {
        val step = if (unit == LoadUnit.AMPS) GameConfig.LOAD_STEP_AMPS else GameConfig.LOAD_STEP_WATTS
        val max = if (unit == LoadUnit.AMPS) GameConfig.MAX_LOAD_AMPS else GameConfig.MAX_LOAD_WATTS
        return (Math.round(value / step) * step).coerceIn(0.0, max)
    }

    private fun MutableStateFlow<GameUiState>.update(block: (GameUiState) -> GameUiState) {
        val next = block(value)
        val results = calculateDrop(toRun(next), SampleData.wireTable)
        value = if (next.held) {
            next.copy(currentAmps = toRun(next).currentAmps)
        } else {
            next.copy(
                results = results,
                recommended = recommendWire(results),
                currentAmps = toRun(next).currentAmps
            )
        }
    }

    private fun toRun(state: GameUiState): WiringRun = WiringRun(
        id = "draft",
        name = "",
        systemVoltage = state.systemVoltage,
        material = state.material,
        loadValue = state.loadValue,
        loadUnit = state.loadUnit,
        lengthMeters = state.lengthMeters.coerceAtLeast(0.01),
        lossLimitPercent = state.lossLimitPercent
    )
}
