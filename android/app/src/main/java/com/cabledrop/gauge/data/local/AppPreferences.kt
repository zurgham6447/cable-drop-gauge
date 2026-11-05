package com.cabledrop.gauge.data.local

import android.content.Context
import com.cabledrop.gauge.core.config.GameConfig
import com.cabledrop.gauge.domain.model.AppSettings
import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.LoadPoint
import com.cabledrop.gauge.domain.model.LoadUnit
import com.cabledrop.gauge.domain.model.SizeSystem
import com.cabledrop.gauge.domain.model.WiringRun

class AppPreferences(context: Context) {

    private val prefs = context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun isSeeded(): Boolean = prefs.getBoolean(KEY_SEEDED, false)

    fun markSeeded() {
        prefs.edit().putBoolean(KEY_SEEDED, true).apply()
    }

    fun readRuns(): List<WiringRun> =
        prefs.getString(KEY_RUNS, "").orEmpty()
            .split(RECORD)
            .filter { it.isNotBlank() }
            .mapNotNull { decodeRun(it) }

    fun writeRuns(runs: List<WiringRun>) {
        prefs.edit().putString(KEY_RUNS, runs.joinToString(RECORD) { encodeRun(it) }).apply()
    }

    fun readLoadPoints(): List<LoadPoint> =
        prefs.getString(KEY_LOADS, "").orEmpty()
            .split(RECORD)
            .filter { it.isNotBlank() }
            .mapNotNull { decodeLoad(it) }

    fun writeLoadPoints(points: List<LoadPoint>) {
        prefs.edit().putString(KEY_LOADS, points.joinToString(RECORD) { encodeLoad(it) }).apply()
    }

    fun readActiveRunId(): String = prefs.getString(KEY_ACTIVE, "").orEmpty()

    fun writeActiveRunId(id: String) {
        prefs.edit().putString(KEY_ACTIVE, id).apply()
    }

    fun readSettings(): AppSettings = AppSettings(
        sizeSystem = sizeSystemOf(prefs.getString(KEY_SIZE_SYSTEM, null)),
        lossLimitPercent = prefs.getInt(KEY_LOSS_LIMIT, GameConfig.DEFAULT_LOSS_LIMIT),
        material = materialOf(prefs.getString(KEY_MATERIAL, null)),
        systemVoltage = prefs.getInt(KEY_VOLTAGE, GameConfig.DEFAULT_VOLTAGE),
        lightMode = prefs.getBoolean(KEY_LIGHT_MODE, false)
    )

    fun writeSettings(settings: AppSettings) {
        prefs.edit()
            .putString(KEY_SIZE_SYSTEM, settings.sizeSystem.name)
            .putInt(KEY_LOSS_LIMIT, settings.lossLimitPercent)
            .putString(KEY_MATERIAL, settings.material.name)
            .putInt(KEY_VOLTAGE, settings.systemVoltage)
            .putBoolean(KEY_LIGHT_MODE, settings.lightMode)
            .apply()
    }

    private fun encodeRun(run: WiringRun): String = listOf(
        run.id,
        sanitize(run.name),
        run.systemVoltage.toString(),
        run.material.name,
        run.loadValue.toString(),
        run.loadUnit.name,
        run.lengthMeters.toString(),
        run.lossLimitPercent.toString()
    ).joinToString(FIELD)

    private fun decodeRun(raw: String): WiringRun? {
        val parts = raw.split(FIELD)
        if (parts.size < 8) return null
        val voltage = parts[2].toIntOrNull() ?: return null
        val load = parts[4].toDoubleOrNull() ?: return null
        val length = parts[6].toDoubleOrNull() ?: return null
        val limit = parts[7].toIntOrNull() ?: return null
        return WiringRun(
            id = parts[0],
            name = parts[1],
            systemVoltage = voltage,
            material = materialOf(parts[3]),
            loadValue = load,
            loadUnit = unitOf(parts[5]),
            lengthMeters = length,
            lossLimitPercent = limit
        )
    }

    private fun encodeLoad(point: LoadPoint): String = listOf(
        point.id,
        sanitize(point.name),
        point.watts.toString(),
        point.distanceMeters.toString()
    ).joinToString(FIELD)

    private fun decodeLoad(raw: String): LoadPoint? {
        val parts = raw.split(FIELD)
        if (parts.size < 4) return null
        val watts = parts[2].toDoubleOrNull() ?: return null
        val distance = parts[3].toDoubleOrNull() ?: return null
        return LoadPoint(id = parts[0], name = parts[1], watts = watts, distanceMeters = distance)
    }

    private fun sanitize(value: String): String =
        value.replace(FIELD, " ").replace(RECORD, " ").trim()

    private fun materialOf(value: String?): ConductorMaterial =
        ConductorMaterial.entries.firstOrNull { it.name == value } ?: ConductorMaterial.COPPER

    private fun unitOf(value: String?): LoadUnit =
        LoadUnit.entries.firstOrNull { it.name == value } ?: LoadUnit.WATTS

    private fun sizeSystemOf(value: String?): SizeSystem =
        SizeSystem.entries.firstOrNull { it.name == value } ?: SizeSystem.AWG

    private companion object {
        const val STORE = "cable_drop_gauge"
        const val FIELD = "^"
        const val RECORD = "\n"
        const val KEY_RUNS = "runs"
        const val KEY_LOADS = "load_points"
        const val KEY_ACTIVE = "active_run"
        const val KEY_SEEDED = "seeded"
        const val KEY_SIZE_SYSTEM = "size_system"
        const val KEY_LOSS_LIMIT = "loss_limit"
        const val KEY_MATERIAL = "material"
        const val KEY_VOLTAGE = "voltage"
        const val KEY_LIGHT_MODE = "light_mode"
    }
}
