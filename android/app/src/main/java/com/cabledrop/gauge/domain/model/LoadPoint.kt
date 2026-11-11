package com.cabledrop.gauge.domain.model

data class LoadPoint(
    val id: String,
    val name: String,
    val watts: Double,
    val distanceMeters: Double
)
