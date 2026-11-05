package com.cabledrop.gauge.domain.usecase

import com.cabledrop.gauge.core.config.GameConfig
import com.cabledrop.gauge.domain.model.FuseHint

class SuggestFuseUseCase {

    operator fun invoke(currentAmps: Double, wireAmpacity: Double, ratings: List<Double>): FuseHint {
        val target = currentAmps * GameConfig.FUSE_MARGIN
        val chosen = ratings.firstOrNull { it >= target } ?: ratings.last()
        if (chosen <= wireAmpacity) {
            return FuseHint(amps = chosen, limitedByWire = false)
        }
        val clamped = ratings.lastOrNull { it <= wireAmpacity } ?: ratings.first()
        return FuseHint(amps = clamped, limitedByWire = true)
    }
}
