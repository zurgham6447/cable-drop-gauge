package com.cabledrop.gauge.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cabledrop.gauge.R
import com.cabledrop.gauge.domain.model.SizeSystem
import com.cabledrop.gauge.domain.model.WireSize
import java.util.Locale

fun decimals(value: Double, digits: Int): String =
    String.format(Locale.US, "%." + digits + "f", value)

fun compactAmps(value: Double): String =
    if (value >= 10.0) decimals(value, 0) else decimals(value, 1)

@Composable
fun wireLabel(size: WireSize, system: SizeSystem): String =
    if (system == SizeSystem.AWG) {
        stringResource(R.string.fmt_awg, size.awg)
    } else {
        stringResource(R.string.fmt_mm2, decimals(size.metricMm2, 1))
    }

@Composable
fun voltsLabel(value: Double): String = stringResource(R.string.fmt_volts, decimals(value, 2))

@Composable
fun percentLabel(value: Double): String = stringResource(R.string.fmt_percent, decimals(value, 1))

@Composable
fun ampsLabel(value: Double): String = stringResource(R.string.fmt_amps, decimals(value, 1))

@Composable
fun wattsLabel(value: Double): String = stringResource(R.string.fmt_watts, decimals(value, 0))

@Composable
fun metersLabel(value: Double): String = stringResource(R.string.fmt_meters, decimals(value, 1))
