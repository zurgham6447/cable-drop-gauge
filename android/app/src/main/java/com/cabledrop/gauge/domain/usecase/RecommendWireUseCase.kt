package com.cabledrop.gauge.domain.usecase

import com.cabledrop.gauge.domain.model.DropResult

class RecommendWireUseCase {

    operator fun invoke(results: List<DropResult>): DropResult? =
        results.filter { it.passes }.minByOrNull { it.size.areaMm2 }
}
