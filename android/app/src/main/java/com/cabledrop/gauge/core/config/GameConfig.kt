package com.cabledrop.gauge.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val DEFAULT_VOLTAGE = 12
    const val DEFAULT_LOSS_LIMIT = 3
    const val DEFAULT_LOAD_WATTS = 60.0
    const val DEFAULT_LENGTH_METERS = 8.0

    const val MIN_LENGTH_METERS = 0.5
    const val MAX_LENGTH_METERS = 60.0
    const val LENGTH_STEP = 0.5

    const val MIN_LOAD_WATTS = 5.0
    const val MAX_LOAD_WATTS = 2000.0
    const val LOAD_STEP_WATTS = 5.0

    const val MIN_LOAD_AMPS = 0.5
    const val MAX_LOAD_AMPS = 120.0
    const val LOAD_STEP_AMPS = 0.5

    const val HOLD_REPEAT_DELAY_MS = 380L
    const val HOLD_REPEAT_INTERVAL_MS = 120L

    const val FUSE_MARGIN = 1.25
    const val SUPPLY_HEADROOM = 1.25
    const val SUPPLY_ROUND_WATTS = 5.0

    const val MIN_DIAGRAM_DISTANCE = 0.5
    const val MAX_DIAGRAM_DISTANCE = 60.0
    const val DIAGRAM_DISTANCE_STEP = 0.5
    const val MIN_LOAD_POINT_WATTS = 1.0
    const val MAX_LOAD_POINT_WATTS = 500.0
    const val LOAD_POINT_STEP_WATTS = 1.0

    val VOLTAGE_OPTIONS = listOf(6, 12, 24, 48)
    val LOSS_LIMIT_OPTIONS = listOf(2, 3, 5, 10)
}
