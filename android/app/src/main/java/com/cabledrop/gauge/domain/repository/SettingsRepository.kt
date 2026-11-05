package com.cabledrop.gauge.domain.repository

import com.cabledrop.gauge.domain.model.AppSettings
import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.SizeSystem
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<AppSettings>

    fun setSizeSystem(system: SizeSystem)
    fun setLossLimit(percent: Int)
    fun setMaterial(material: ConductorMaterial)
    fun setSystemVoltage(volts: Int)
    fun setLightMode(enabled: Boolean)
}
