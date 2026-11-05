package com.cabledrop.gauge.data.repository

import com.cabledrop.gauge.data.local.AppPreferences
import com.cabledrop.gauge.domain.model.AppSettings
import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.SizeSystem
import com.cabledrop.gauge.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(private val preferences: AppPreferences) : SettingsRepository {

    private val _settings = MutableStateFlow(preferences.readSettings())
    override val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    override fun setSizeSystem(system: SizeSystem) = persist(_settings.value.copy(sizeSystem = system))

    override fun setLossLimit(percent: Int) =
        persist(_settings.value.copy(lossLimitPercent = percent.coerceIn(1, 10)))

    override fun setMaterial(material: ConductorMaterial) =
        persist(_settings.value.copy(material = material))

    override fun setSystemVoltage(volts: Int) = persist(_settings.value.copy(systemVoltage = volts))

    override fun setLightMode(enabled: Boolean) = persist(_settings.value.copy(lightMode = enabled))

    private fun persist(settings: AppSettings) {
        _settings.value = settings
        preferences.writeSettings(settings)
    }
}
