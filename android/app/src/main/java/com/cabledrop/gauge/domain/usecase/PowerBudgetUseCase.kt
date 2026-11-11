package com.cabledrop.gauge.domain.usecase

import com.cabledrop.gauge.core.config.GameConfig
import com.cabledrop.gauge.domain.model.PowerBudget
import kotlin.math.ceil

class PowerBudgetUseCase {

    operator fun invoke(totalWatts: Double): PowerBudget {
        val step = GameConfig.SUPPLY_ROUND_WATTS
        val supply = ceil(totalWatts * GameConfig.SUPPLY_HEADROOM / step) * step
        return PowerBudget(totalWatts = totalWatts, supplyWatts = supply)
    }
}
