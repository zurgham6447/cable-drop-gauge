package com.cabledrop.gauge.presentation.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabledrop.gauge.core.navigation.Screen
import com.cabledrop.gauge.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppViewModel(settingsRepository: SettingsRepository) : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _backStack = MutableStateFlow<List<Screen>>(emptyList())
    val backStack: StateFlow<List<Screen>> = _backStack.asStateFlow()

    val lightMode: StateFlow<Boolean> = settingsRepository.settings
        .map { it.lightMode }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = settingsRepository.settings.value.lightMode
        )

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current == target) return
        if (current != Screen.Splash) {
            _backStack.value = _backStack.value + current
        }
        _screen.value = target
    }

    fun replaceRoot(target: Screen) {
        _backStack.value = emptyList()
        _screen.value = target
    }

    fun back() {
        val stack = _backStack.value
        val previous = stack.lastOrNull() ?: return
        _backStack.value = stack.dropLast(1)
        _screen.value = previous
    }
}
