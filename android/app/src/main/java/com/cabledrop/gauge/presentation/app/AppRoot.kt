package com.cabledrop.gauge.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cabledrop.gauge.core.di.ServiceLocator
import com.cabledrop.gauge.core.navigation.Screen
import com.cabledrop.gauge.core.theme.AppTheme
import com.cabledrop.gauge.presentation.diagram.RunDiagramPanel
import com.cabledrop.gauge.presentation.game.GameScreen
import com.cabledrop.gauge.presentation.gameover.GameOverScreen
import com.cabledrop.gauge.presentation.menu.MenuScreen
import com.cabledrop.gauge.presentation.safety.SafetyNotesPanel
import com.cabledrop.gauge.presentation.settings.WireSettingsPanel
import com.cabledrop.gauge.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val backStack by appViewModel.backStack.collectAsStateWithLifecycle()
    val lightMode by appViewModel.lightMode.collectAsStateWithLifecycle()

    AppTheme(darkTheme = !lightMode) {
        BackHandler(enabled = backStack.isNotEmpty()) { appViewModel.back() }
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                fadeIn(tween(220)) + slideInVertically { height -> height / 12 } togetherWith
                    fadeOut(tween(180))
            },
            label = "screenHost"
        ) { target ->
            when (target) {
                Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.replaceRoot(Screen.Menu) }
                )

                Screen.Menu -> MenuScreen(
                    onStartRun = { appViewModel.navigate(Screen.Game) },
                    onDiagram = { appViewModel.navigate(Screen.Diagram) },
                    onSettings = { appViewModel.navigate(Screen.Settings) },
                    onSafety = { appViewModel.navigate(Screen.Safety) }
                )

                Screen.Game -> GameScreen(
                    onHome = { appViewModel.replaceRoot(Screen.Menu) },
                    onDiagram = { appViewModel.navigate(Screen.Diagram) },
                    onCalculated = { runId -> appViewModel.navigate(Screen.GameOver(runId)) }
                )

                is Screen.GameOver -> GameOverScreen(
                    onCalculateAgain = { appViewModel.back() },
                    onMenu = { appViewModel.replaceRoot(Screen.Menu) }
                )

                Screen.Diagram -> RunDiagramPanel(
                    onBack = { appViewModel.back() },
                    onHome = { appViewModel.replaceRoot(Screen.Menu) }
                )

                Screen.Settings -> WireSettingsPanel(
                    onBack = { appViewModel.back() },
                    onHome = { appViewModel.replaceRoot(Screen.Menu) }
                )

                Screen.Safety -> SafetyNotesPanel(
                    onBack = { appViewModel.back() },
                    onHome = { appViewModel.replaceRoot(Screen.Menu) }
                )
            }
        }
    }
}
