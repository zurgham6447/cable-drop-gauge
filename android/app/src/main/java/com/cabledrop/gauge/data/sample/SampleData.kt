package com.cabledrop.gauge.data.sample

import com.cabledrop.gauge.R
import com.cabledrop.gauge.domain.model.ConductorMaterial
import com.cabledrop.gauge.domain.model.LoadPoint
import com.cabledrop.gauge.domain.model.LoadUnit
import com.cabledrop.gauge.domain.model.SafetyNote
import com.cabledrop.gauge.domain.model.WireSize
import com.cabledrop.gauge.domain.model.WiringRun

object SampleData {

    val wireTable: List<WireSize> = listOf(
        WireSize(awg = 20, areaMm2 = 0.52, metricMm2 = 0.5, ampacityAmps = 5.0),
        WireSize(awg = 18, areaMm2 = 0.82, metricMm2 = 0.75, ampacityAmps = 7.0),
        WireSize(awg = 16, areaMm2 = 1.31, metricMm2 = 1.5, ampacityAmps = 10.0),
        WireSize(awg = 14, areaMm2 = 2.08, metricMm2 = 2.5, ampacityAmps = 15.0),
        WireSize(awg = 12, areaMm2 = 3.31, metricMm2 = 4.0, ampacityAmps = 20.0),
        WireSize(awg = 10, areaMm2 = 5.26, metricMm2 = 6.0, ampacityAmps = 30.0),
        WireSize(awg = 8, areaMm2 = 8.37, metricMm2 = 10.0, ampacityAmps = 40.0),
        WireSize(awg = 6, areaMm2 = 13.30, metricMm2 = 16.0, ampacityAmps = 55.0),
        WireSize(awg = 4, areaMm2 = 21.20, metricMm2 = 25.0, ampacityAmps = 70.0),
        WireSize(awg = 2, areaMm2 = 33.60, metricMm2 = 35.0, ampacityAmps = 95.0)
    )

    val fuseRatings: List<Double> = listOf(
        1.0, 2.0, 3.0, 5.0, 7.5, 10.0, 15.0, 20.0, 25.0, 30.0, 40.0, 50.0, 60.0, 80.0, 100.0
    )

    val safetyNotes: List<SafetyNote> = listOf(
        SafetyNote(1, R.string.note_1_title, R.string.note_1_body),
        SafetyNote(2, R.string.note_2_title, R.string.note_2_body),
        SafetyNote(3, R.string.note_3_title, R.string.note_3_body),
        SafetyNote(4, R.string.note_4_title, R.string.note_4_body),
        SafetyNote(5, R.string.note_5_title, R.string.note_5_body),
        SafetyNote(6, R.string.note_6_title, R.string.note_6_body)
    )

    val seededRuns: List<WiringRun> = listOf(
        WiringRun(
            id = "seed-garden",
            name = "GARDEN PATH LIGHTS",
            systemVoltage = 12,
            material = ConductorMaterial.COPPER,
            loadValue = 36.0,
            loadUnit = LoadUnit.WATTS,
            lengthMeters = 14.0,
            lossLimitPercent = 3
        ),
        WiringRun(
            id = "seed-camper",
            name = "CAMPER FRIDGE FEED",
            systemVoltage = 12,
            material = ConductorMaterial.COPPER,
            loadValue = 4.5,
            loadUnit = LoadUnit.AMPS,
            lengthMeters = 5.0,
            lossLimitPercent = 3
        ),
        WiringRun(
            id = "seed-bench",
            name = "LED STRIP BENCH",
            systemVoltage = 24,
            material = ConductorMaterial.COPPER,
            loadValue = 48.0,
            loadUnit = LoadUnit.WATTS,
            lengthMeters = 6.0,
            lossLimitPercent = 5
        )
    )

    val seededLoadPoints: List<LoadPoint> = listOf(
        LoadPoint(id = "load-strip", name = "LED STRIP A", watts = 8.0, distanceMeters = 3.0),
        LoadPoint(id = "load-fan", name = "VENT FAN", watts = 10.0, distanceMeters = 6.0),
        LoadPoint(id = "load-lamp", name = "BENCH LAMP", watts = 6.0, distanceMeters = 9.5)
    )
}
