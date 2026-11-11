package com.cabledrop.gauge.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.SizeSystem
import com.cabledrop.gauge.domain.repository.RunRepository
import com.cabledrop.gauge.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class WireSettingsViewModel(
    private val runRepository: RunRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WireSettingsUiState())
    val state: StateFlow<WireSettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                settingsRepository.settings,
                runRepository.savedRuns
            ) { settings, runs ->
                WireSettingsUiState(
                    sizeSystem = settings.sizeSystem,
                    lossLimitPercent = settings.lossLimitPercent,
                    material = settings.material,
                    systemVoltage = settings.systemVoltage,
                    lightMode = settings.lightMode,
                    savedRuns = runs.size,
                    confirmVisible = _state.value.confirmVisible
                )
            }.collect { snapshot -> _state.value = snapshot }
        }
    }

    fun setSizeSystem(system: SizeSystem) = settingsRepository.setSizeSystem(system)

    fun setLossLimit(percent: Int) = settingsRepository.setLossLimit(percent)

    fun setMaterial(material: ConductorMaterial) = settingsRepository.setMaterial(material)

    fun setSystemVoltage(volts: Int) = settingsRepository.setSystemVoltage(volts)

    fun setLightMode(enabled: Boolean) = settingsRepository.setLightMode(enabled)

    fun askClear() {
        _state.value = _state.value.copy(confirmVisible = true)
    }

    fun dismissClear() {
        _state.value = _state.value.copy(confirmVisible = false)
    }

    fun confirmClear() {
        runRepository.clearRuns()
        dismissClear()
    }
}
