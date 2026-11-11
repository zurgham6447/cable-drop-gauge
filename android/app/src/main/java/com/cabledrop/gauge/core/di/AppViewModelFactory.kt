package com.cabledrop.gauge.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.cabledrop.gauge.presentation.app.AppViewModel
import com.cabledrop.gauge.presentation.diagram.RunDiagramViewModel
import com.cabledrop.gauge.presentation.game.GameViewModel
import com.cabledrop.gauge.presentation.gameover.GameOverViewModel
import com.cabledrop.gauge.presentation.menu.MenuViewModel
import com.cabledrop.gauge.presentation.safety.SafetyNotesViewModel
import com.cabledrop.gauge.presentation.settings.WireSettingsViewModel
import com.cabledrop.gauge.presentation.splash.SplashViewModel

class AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val instance: ViewModel = when (modelClass) {
            AppViewModel::class.java -> AppViewModel(ServiceLocator.settingsRepository)
            SplashViewModel::class.java -> SplashViewModel()
            MenuViewModel::class.java -> MenuViewModel(
                ServiceLocator.runRepository,
                ServiceLocator.settingsRepository,
                ServiceLocator.calculateDrop,
                ServiceLocator.recommendWire
            )
            GameViewModel::class.java -> GameViewModel(
                ServiceLocator.runRepository,
                ServiceLocator.settingsRepository,
                ServiceLocator.calculateDrop,
                ServiceLocator.recommendWire
            )
            GameOverViewModel::class.java -> GameOverViewModel(
                ServiceLocator.runRepository,
                ServiceLocator.settingsRepository,
                ServiceLocator.calculateDrop,
                ServiceLocator.recommendWire,
                ServiceLocator.suggestFuse,
                ServiceLocator.powerBudget
            )
            RunDiagramViewModel::class.java -> RunDiagramViewModel(
                ServiceLocator.runRepository,
                ServiceLocator.settingsRepository,
                ServiceLocator.calculateDrop,
                ServiceLocator.recommendWire,
                ServiceLocator.diagramDrop
            )
            WireSettingsViewModel::class.java -> WireSettingsViewModel(
                ServiceLocator.runRepository,
                ServiceLocator.settingsRepository
            )
            SafetyNotesViewModel::class.java -> SafetyNotesViewModel()
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return instance as T
    }
}
