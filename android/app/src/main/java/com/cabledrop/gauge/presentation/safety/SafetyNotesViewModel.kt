package com.cabledrop.gauge.presentation.safety

import androidx.lifecycle.ViewModel
import com.cabledrop.gauge.data.sample.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SafetyNotesViewModel : ViewModel() {

    private val _state = MutableStateFlow(SafetyNotesUiState(notes = SampleData.safetyNotes))
    val state: StateFlow<SafetyNotesUiState> = _state.asStateFlow()
}
