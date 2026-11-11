package com.cabledrop.gauge.presentation.safety

import com.cabledrop.gauge.domain.model.SafetyNote

data class SafetyNotesUiState(
    val notes: List<SafetyNote> = emptyList()
)
